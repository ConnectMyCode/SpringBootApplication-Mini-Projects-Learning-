# FitClub deployment: React + Spring Boot + AWS RDS

We'll deploy your existing application with Docker, Docker Hub, two EC2 instances, and RDS, keeping code changes to a minimum.

One important security correction before you start: an Elastic IP and CORS do not secure the backend. CORS controls which browser origins can read responses; it is not a network firewall. AWS Security Groups must enforce which machines can connect to which services.

## 1. Target architecture
    
    Public Internet
    
    Users access the frontend over HTTP/HTTPS
    
    Frontend EC2
    
    Public subnet · Elastic IP · Docker · React/Vite served by Nginx
    
    Inbound: public HTTPS (443); optionally HTTP (80) for redirect. SSH restricted to your IP.
    
    Frontend browser requests → backend API
    
    Backend EC2
    
    Private subnet · Docker · Spring Boot JAR
    
    Inbound: API port (for example, 8080) only from the Frontend EC2 security group. No public IP or inbound internet access.
    
    Private database connection
    
    Amazon RDS
    
    Private subnets · Not publicly accessible
    
    Inbound: database port (for example, MySQL 3306) only from the Backend EC2 security group.
    
    Important browser detail: React runs in the user's browser, even when its files are served by the Frontend EC2. API requests originate from the browser, not from the Nginx container. The browser therefore needs a reachable backend API URL. With your strict rules, the cleanest approach is to expose the backend through a controlled HTTPS reverse proxy or load balancer while keeping the actual Spring Boot EC2 private. I'll keep the steps below aligned with your requested two EC2 instances and call out where this matters.

## 2. Configure AWS networking and security groups

Do this before building and deploying the containers.

| Resource     | Inbound rule                    | Source                      |
| ------------ | ------------------------------- | --------------------------- |
| Frontend EC2 | TCP 443                         | `0.0.0.0/0`                 |
| Frontend EC2 | TCP 80, if redirecting to HTTPS | `0.0.0.0/0`                 |
| Frontend EC2 | TCP 22, if using SSH            | Your public IP `/32` only   |
| Backend EC2  | TCP 8080                        | Frontend EC2 security group |
| RDS MySQL    | TCP 3306                        | Backend EC2 security group  |

Use the actual backend and database ports from your configuration if they differ.

- Frontend EC2: public subnet, Elastic IP, public-facing Nginx.
- Backend EC2: private subnet, no public IP.
- RDS: private DB subnet group and Public access = No.
- Private subnet outbound access: provide a deployment path to Docker Hub, such as a NAT Gateway, or use another controlled image-delivery approach. You can also use AWS Systems Manager instead of opening SSH to the backend.

Do not open ports 8080 or 3306 to `0.0.0.0/0`.

## 3. Create the Spring Boot JAR without embedding secrets

From your backend project directory:

```
./mvnw clean package -DskipTests
```

On Windows Command Prompt:

```
mvnw.cmd clean package -DskipTests
```

Locate the executable JAR in `target/`. Confirm that the build completed successfully.

Before proceeding, check that database credentials are not hardcoded in `application.properties` or another file packaged into the JAR. Use environment-variable placeholders instead, for example:

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Set these variables on the backend EC2 at deployment time. Do not commit the values to Git or bake them into the image. For stronger production secret management, use AWS Secrets Manager or Systems Manager Parameter Store.

## 4. Build the Docker images

### Backend image

Create a `Dockerfile` in the backend project root, adjusting the JAR filename to match the file you built.

```
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/your-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Use the Java version compatible with your existing Spring Boot project. `EXPOSE 8080` documents the application port; it does not make the port publicly accessible.

Build the image:

```
docker build -t YOUR_DOCKERHUB_USERNAME/fitclub-backend:1.0 .
```

### Frontend image

For React + Vite, build the production frontend and serve its generated files through Nginx. Create a frontend Dockerfile that builds the Vite application and copies `dist/` into Nginx's web root.

Critical integration requirement: your current React code uses URLs such as `http://localhost:8080/api/...`. Those URLs will refer to the visitor's own computer after deployment, not your backend EC2. The production frontend must call a relative path such as `/api/...`, with Nginx proxying `/api/` requests to the backend's private address. This is the main integration change you cannot safely skip.

The Nginx configuration on the Frontend EC2 should proxy `/api/` to the private backend address, for example:

```
location /api/ {
    proxy_pass http://BACKEND_PRIVATE_IP:8080/api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```

Replace `BACKEND_PRIVATE_IP` with the backend's private IP. With the trailing `/api/` on both sides, the path is preserved. Configure the proxy in the Nginx server block that serves the frontend.

This design means the browser communicates with the public frontend origin, and Nginx forwards API traffic privately. You avoid exposing the Spring Boot port to the public internet and generally avoid cross-origin requests for this deployment.

Build the frontend image after adding the Nginx configuration:

```
docker build -t YOUR_DOCKERHUB_USERNAME/fitclub-frontend:1.0 .
```

Do not publish a frontend image that still contains the development `localhost:8080` API URLs.

## 5. Push both images to Docker Hub

Log in:

```
docker login
```

Push the backend:

```
docker push YOUR_DOCKERHUB_USERNAME/fitclub-backend:1.0
```

Push the frontend:

```
docker push YOUR_DOCKERHUB_USERNAME/fitclub-frontend:1.0
```

Use a private Docker Hub repository if your images should not be publicly downloadable. If the repositories are private, configure authenticated pulls on both EC2 instances.

## 6. Deploy the backend container on Backend EC2

Connect through AWS Systems Manager or your approved private-access method. Install Docker and pull the image:

```
docker pull YOUR_DOCKERHUB_USERNAME/fitclub-backend:1.0
```

Set the database connection variables using your actual RDS endpoint and database name:

```
export DB_URL='jdbc:mysql://YOUR_RDS_ENDPOINT:3306/YOUR_DATABASE'
export DB_USERNAME='YOUR_DB_USERNAME'
export DB_PASSWORD='YOUR_DB_PASSWORD'
```

Do not put real credentials into shell history, deployment scripts committed to Git, or screenshots. For production, supply these securely through AWS Secrets Manager or Parameter Store.

Run the container:

```
docker run -d \
  --name fitclub-backend \
  --restart unless-stopped \
  -p 8080:8080 \
  -e DB_URL \
  -e DB_USERNAME \
  -e DB_PASSWORD \
  YOUR_DOCKERHUB_USERNAME/fitclub-backend:1.0
```

Here, `-p 8080:8080` publishes the port on the EC2 host, but the Backend EC2 security group must still allow inbound 8080 only from the Frontend EC2 security group. Do not create an internet-wide rule.

Check startup logs:

```
docker logs --tail 100 fitclub-backend
```

Confirm that Spring Boot starts and connects to RDS without errors.

## 7. Deploy the frontend container on Frontend EC2

Pull the image:

```
docker pull YOUR_DOCKERHUB_USERNAME/fitclub-frontend:1.0
```

Run Nginx:

```
docker run -d \
  --name fitclub-frontend \
  --restart unless-stopped \
  -p 80:80 \
  YOUR_DOCKERHUB_USERNAME/fitclub-frontend:1.0
```

Configure HTTPS before treating the application as production-ready. You can use a domain with a TLS certificate and redirect HTTP to HTTPS. If you intend to use only the Elastic IP, certificate and HTTPS setup requires extra care; do not assume an ordinary domain certificate can be issued for an IP address.

### What about the Elastic IP and CORS?

- Associate the Elastic IP with the Frontend EC2.
- Configure the frontend API calls to use the same public origin, such as `/api/classes`.
- Configure Nginx to forward `/api/` to the backend's private IP.
- CORS is generally unnecessary for these same-origin requests, though your Spring Security and CSRF settings still need to match your session-based authentication.
- If you instead call the backend directly from the browser, you must expose a publicly reachable backend endpoint and allow browser-origin traffic. That would conflict with your strict private-backend requirement.

## 8. Test the complete deployment

Run the tests in this order so failures are easier to isolate.

Test 1 — Frontend

Open `http://YOUR_ELASTIC_IP` temporarily, or the HTTPS domain once configured. Confirm that the React application loads.

Test 2 — Backend through Nginx

From the browser's developer tools, inspect the Network tab. Confirm that requests go to the frontend origin under `/api/...` and return the expected responses. Do not test using `localhost:8080` from your browser.

Test 3 — Database integration

Register a user, log in, retrieve classes, book a class, add a favorite, and retrieve bookings. Verify the relevant database records and confirm that session cookies work across requests.

Test 4 — Verify private access

From an external machine, verify that the backend's private IP and RDS endpoint are not directly reachable. Confirm that backend inbound access is limited to the frontend security group and database inbound access is limited to the backend security group.

Useful container commands on either EC2:

```
docker ps
docker logs --tail 100 fitclub-frontend
docker logs --tail 100 fitclub-backend
```

## Deployment checklist

Progress

0 of 10

Build the Spring Boot JAR without embedded secrets

Create and push the backend image

Configure private backend networking and RDS security group

Update production frontend API paths and configure Nginx proxy

Create and push the frontend image

Pull and run the backend container with secure DB configuration

Pull and run the frontend container

Associate the Elastic IP and configure HTTPS

Test authentication, classes, bookings, favorites, and session cookies

Verify that backend and RDS cannot be reached directly from the internet

One final security note: a private backend EC2 with no public IP cannot be managed or pull images without a configured private-access and outbound-connectivity path. Set up Systems Manager access and the required VPC endpoints or NAT routing before deployment. This avoids opening the backend to the internet just to install Docker or pull your image.