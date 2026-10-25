# FitClub deployment plan: Docker Hub → AWS EC2 → Amazon RDS

We'll deploy your existing FitClub application using the architecture you decided on. We'll work in small stages, verify each stage, and only proceed when the current one works.

## 1. Target architecture

User's browser

HTTPS · Public access

Frontend EC2 · t3.micro

Docker container

React production build + Nginx

Public ports 80/443 · Nginx forwards `/api/` requests

Backend EC2 · t3.small

Docker container

Spring Boot REST API

Port 8080 · Inbound only from frontend security group

Amazon RDS · MySQL

Private access · Port 3306 · Managed database

Docker Hub is the image registry, not part of the live request path. We build and push images from your development machine; each EC2 instance pulls and runs its own image.

## 2. Deployment stages

1. Inspect existing configuration

   Check the backend `Dockerfile`, `pom.xml`, `.env` variable names, and frontend `package.json` and API URL configuration. We won't overwrite working files blindly.
2. Prepare the backend image

   Ensure Spring Boot reads database settings from environment variables. Build the JAR with Maven, create a Docker image, and test that no actual credentials are embedded in the image.
3. Push the backend image to Docker Hub

   Log in to Docker Hub, tag the image with your repository name, and push it.
4. Deploy the backend to its EC2

   Pull the image, provide database settings at runtime, start the container, and test the connection to RDS.
5. Prepare the frontend image

   Build React's production files and configure Nginx to serve them and proxy `/api/` to the backend's private IP or private DNS.
6. Push and deploy the frontend image

   Push the image to Docker Hub, pull it on the frontend EC2, and run the container with the required ports.
7. End-to-end verification

   Test login, session handling, API requests, database access, browser networking, and application logs.

## 3. Precautions before deployment

| Area                 | Precaution                                                                                                              |
| -------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| Database credentials | Never put real credentials in the JAR, Dockerfile, image layers, GitHub, or Docker Hub. Pass them at container runtime. |
| `.env`               | Keep it out of Git and Docker build context. Use a separate production environment file on the backend EC2.             |
| RDS                  | Keep public access disabled. Allow inbound MySQL port `3306` only from the backend EC2 security group.                  |
| Backend              | Allow inbound `8080` only from the frontend EC2 security group.                                                         |
| Frontend             | Browser requests should use relative API paths such as `/api/classes`, not `localhost` or the backend's private IP.     |
| Sessions             | Since your application uses HTTP sessions, verify cookie settings and session behavior through the Nginx proxy.         |
| Images               | Use explicit image tags rather than relying on `latest`, so you can identify which version is deployed.                 |
| Recovery             | Keep your source code and environment configuration backed up before changing existing deployment files.                |

Important: HTTPS requires a TLS certificate and configuration; opening port `443` alone does not enable HTTPS. We can initially verify the application over HTTP and configure HTTPS afterward.

## 4. Start execution — Step 1: Inspect existing files

Your backend screenshot already shows a `Dockerfile` and `docker-compose.yml`. We'll inspect the existing setup before deciding what needs changing.

### A. Backend

Open the `Spring14-fitclub-mvc` project root and share the contents of:

1. `Dockerfile`
2. `.gitignore`
3. `application_example.properties` — if it contains no real credentials; otherwise redact them.

You do not need to share `.env` or any actual credentials.

Also run this command from the backend project root in PowerShell:

```
git status --short
```

This lets us see whether there are existing local changes before editing files.

### B. Frontend

Open `spring14frontend/package.json` and share its contents. It should contain the scripts and dependencies needed to determine the correct production build command.

Also tell me whether your React code calls the backend using:

```
fetch("http://localhost:8080/...")
```

or uses a relative path, such as:

```
fetch("/api/...")
```

If you use Axios or a configured API client instead, tell me which one.

Stop after this inspection step. Once I have these details, I'll provide the exact changes for your existing project, starting with the backend Docker image. We won't proceed to EC2 deployment until the image is built and verified locally.