Absolutely. For **Spring REST API**, you should understand HTTP first because Spring annotations are essentially helping you map **HTTP requests → Java methods** and Java results → **HTTP responses**.

# HTTP — Complete Beginner-to-Intermediate Guide for Spring REST
NOTE: Revise about the HTTP if neccessary can look at the last part of this pdf...
## 1. What is HTTP?

**HTTP = HyperText Transfer Protocol.**

It is the protocol used for communication between a **client** and a **server**.

For a REST API:

```text
Client
  |
  | HTTP Request
  ↓
Spring Boot Server
  |
  | HTTP Response
  ↓
Client
```

For example, Postman sends:

```http
GET /medicines/101
```

Spring Boot receives it, finds the appropriate controller method, executes it, and sends a response.

---

# 2. HTTP Request

An HTTP request mainly contains:

```text
HTTP Request
│
├── Method
├── URL
├── Headers
└── Body
```

Example:

```http
POST /medicines
Content-Type: application/json

{
    "name": "Paracetamol",
    "quantity": 20
}
```

Here:

| Part   | Example        | Purpose                     |
| ------ | -------------- | --------------------------- |
| Method | `POST`         | What operation client wants |
| URL    | `/medicines`   | Which resource              |
| Header | `Content-Type` | Information about request   |
| Body   | JSON           | Data sent to server         |

---

# 3. HTTP Methods

These are extremely important for REST APIs.

| Method   | Common purpose        | Example         |
| -------- | --------------------- | --------------- |
| `GET`    | Retrieve data         | Get medicine    |
| `POST`   | Create data           | Add medicine    |
| `PUT`    | Replace/update data   | Update medicine |
| `PATCH`  | Partially update data | Change quantity |
| `DELETE` | Delete data           | Delete medicine |

Example API:

```text
GET     /medicines
GET     /medicines/101
POST    /medicines
PUT     /medicines/101
PATCH   /medicines/101
DELETE  /medicines/101
```

---

# 4. GET

Used to retrieve data.

### Client sends

```http
GET /medicines/101
```

### Spring Boot

```java
@GetMapping("/medicines/{id}")
public Medicine getMedicine(@PathVariable Long id) {
    return medicineService.getMedicine(id);
}
```

Flow:

```text
GET /medicines/101
       ↓
@GetMapping
       ↓
@PathVariable = 101
       ↓
Service
       ↓
Database
       ↓
Medicine
       ↓
JSON Response
```

---

# 5. POST

Used to create a new resource.

Request:

```http
POST /medicines
Content-Type: application/json

{
    "name": "Paracetamol",
    "quantity": 20
}
```

Spring:

```java
@PostMapping("/medicines")
public Medicine addMedicine(@RequestBody Medicine medicine) {
    return medicineService.addMedicine(medicine);
}
```

Here:

```java
@RequestBody
```

means:

> Take the data from the HTTP request body and convert it into a Java object.

---

# 6. PUT

Used when you want to update/replace a resource.

```http
PUT /medicines/101
```

```java
@PutMapping("/medicines/{id}")
public Medicine updateMedicine(
        @PathVariable Long id,
        @RequestBody Medicine medicine) {

    return medicineService.updateMedicine(id, medicine);
}
```

---

# 7. PATCH

Used for a **partial update**.

For example, only quantity needs to change:

```http
PATCH /medicines/101

{
    "quantity": 50
}
```

Spring:

```java
@PatchMapping("/medicines/{id}")
public Medicine updateQuantity(
        @PathVariable Long id,
        @RequestBody Medicine medicine) {

    return medicineService.updateQuantity(id, medicine);
}
```

---

# 8. DELETE

Used to delete a resource.

```http
DELETE /medicines/101
```

Spring:

```java
@DeleteMapping("/medicines/{id}")
public void deleteMedicine(@PathVariable Long id) {
    medicineService.deleteMedicine(id);
}
```

---

# 9. HTTP URL

A URL identifies the resource.

Example:

```text
http://localhost:8080/medicines/101
```

Breakdown:

```text
http://
   ↓
Protocol

localhost
   ↓
Host

8080
   ↓
Port

/medicines/101
   ↓
Path
```

---

# 10. Path Variable

A path variable is part of the URL path.

```text
/medicines/101
```

Here `101` is the ID.

Spring:

```java
@GetMapping("/medicines/{id}")
public Medicine getMedicine(@PathVariable Long id) {
    // id = 101
}
```

Important:

```java
/{id}
```

matches:

```text
/101
/102
/500
```

---

# 11. Request Parameter

Request parameters come after `?`.

```text
/medicines?name=Paracetamol
```

Spring:

```java
@GetMapping("/medicines")
public List<Medicine> search(
        @RequestParam String name) {

    return medicineService.search(name);
}
```

Multiple parameters:

```text
/medicines?name=Paracetamol&quantity=20
```

```java
@RequestParam String name
@RequestParam Integer quantity
```

You can also make a parameter optional:

```java
@RequestParam(required = false) String name
```

---

# 12. Request Body

Request body contains data sent by the client.

Example:

```json
{
    "name": "Paracetamol",
    "quantity": 20
}
```

Spring:

```java
@PostMapping("/medicines")
public Medicine addMedicine(
        @RequestBody Medicine medicine) {

    return medicineService.addMedicine(medicine);
}
```

Spring/Jackson essentially performs:

```text
JSON
 ↓
Jackson
 ↓
Java Object
```

This is called **deserialization**.

---

# 13. Response Body

Suppose your method returns:

```java
return medicine;
```

Spring converts the Java object to JSON.

```text
Java Object
     ↓
Jackson
     ↓
JSON
```

This is called **serialization**.

Example:

```java
public Medicine getMedicine() {
    return medicine;
}
```

Response:

```json
{
    "medicineId": 101,
    "name": "Paracetamol",
    "quantity": 20
}
```

---

# 14. `@RestController`

This is one of the most important annotations.

```java
@RestController
public class MedicineController {
}
```

It basically combines:

```java
@Controller
@ResponseBody
```

Meaning that the methods are intended to handle HTTP requests and their return values are written to the HTTP response body.

---

# 15. `@RequestMapping`

Defines a common URL mapping.

```java
@RestController
@RequestMapping("/medicines")
public class MedicineController {
}
```

Then:

```java
@GetMapping
```

means:

```text
GET /medicines
```

And:

```java
@GetMapping("/{id}")
```

means:

```text
GET /medicines/101
```

So:

```java
@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @GetMapping
    public List<Medicine> getAll() {
        // ...
    }

    @GetMapping("/{id}")
    public Medicine getOne(@PathVariable Long id) {
        // ...
    }
}
```

---

# 16. HTTP Headers

Headers contain additional information about the request or response.

Example:

```http
Content-Type: application/json
Authorization: Bearer xyz123
Accept: application/json
```

Common headers:

| Header          | Purpose                          |
| --------------- | -------------------------------- |
| `Content-Type`  | Type of data being sent          |
| `Accept`        | Type of response client wants    |
| `Authorization` | Authentication credentials/token |
| `User-Agent`    | Information about client         |
| `Cookie`        | Cookie data                      |

---

# 17. `@RequestHeader`

You can access a request header.

```java
@GetMapping("/medicines")
public List<Medicine> getMedicines(
        @RequestHeader("Authorization") String token) {

    // use token
}
```

Request:

```http
GET /medicines
Authorization: Bearer abc123
```

Then:

```java
token
```

contains:

```text
Bearer abc123
```

---

# 18. Cookies

HTTP requests can also contain cookies.

Example:

```http
Cookie: sessionId=abc123
```

Spring can access it using:

```java
@CookieValue("sessionId") String sessionId
```

Example:

```java
@GetMapping("/profile")
public String profile(
        @CookieValue("sessionId") String sessionId) {

    return sessionId;
}
```

---

# 19. HTTP Response

A response generally contains:

```text
HTTP Response
│
├── Status Code
├── Headers
└── Body
```

Example:

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 101,
    "name": "Paracetamol"
}
```

---

# 20. HTTP Status Codes

You need to know these very well.

### 2xx — Success

| Status           | Meaning                         |
| ---------------- | ------------------------------- |
| `200 OK`         | Request successful              |
| `201 Created`    | Resource successfully created   |
| `204 No Content` | Successful but no response body |

Example:

```text
POST /medicines
       ↓
Medicine created
       ↓
201 Created
```

---

### 4xx — Client Error

| Status             | Meaning                                       |
| ------------------ | --------------------------------------------- |
| `400 Bad Request`  | Invalid request                               |
| `401 Unauthorized` | Authentication required/failed                |
| `403 Forbidden`    | Authenticated but not allowed                 |
| `404 Not Found`    | Resource doesn't exist                        |
| `409 Conflict`     | Request conflicts with current resource state |

Example:

```text
GET /medicines/999
       ↓
Medicine doesn't exist
       ↓
404 Not Found
```

---

### 5xx — Server Error

| Status                      | Meaning                                |
| --------------------------- | -------------------------------------- |
| `500 Internal Server Error` | Server encountered an unexpected error |
| `503 Service Unavailable`   | Server/service currently unavailable   |

---

# 21. `ResponseEntity`

This is extremely important in Spring REST.

Without `ResponseEntity`:

```java
@GetMapping("/{id}")
public Medicine getMedicine(@PathVariable Long id) {
    return medicine;
}
```

Spring determines much of the response for you.

With `ResponseEntity`:

```java
@GetMapping("/{id}")
public ResponseEntity<Medicine> getMedicine(
        @PathVariable Long id) {

    Medicine medicine = medicineService.getMedicine(id);

    return ResponseEntity.ok(medicine);
}
```

Now you have explicit control over the HTTP response.

For example:

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(medicine);
```

Response:

```text
201 Created
     +
Medicine JSON
```

---

# 22. Common `ResponseEntity` Methods

```java
ResponseEntity.ok(data);
```

→ `200 OK`

```java
ResponseEntity.status(HttpStatus.CREATED).body(data);
```

→ `201 Created`

```java
ResponseEntity.noContent().build();
```

→ `204 No Content`

```java
ResponseEntity.notFound().build();
```

→ `404 Not Found`

---

# 23. `@ResponseStatus`

You can specify a status code.

```java
@ResponseStatus(HttpStatus.CREATED)
@PostMapping
public Medicine addMedicine(
        @RequestBody Medicine medicine) {

    return medicineService.addMedicine(medicine);
}
```

This returns:

```text
201 Created
```

But when you need dynamic status codes or headers, `ResponseEntity` is generally more flexible.

---

# 24. `@ResponseBody`

```java
@ResponseBody
```

means:

> Put the returned value into the HTTP response body instead of resolving it as a view.

For example:

```java
@Controller
public class MedicineController {

    @ResponseBody
    @GetMapping("/medicine")
    public Medicine getMedicine() {
        return medicine;
    }
}
```

`@RestController` effectively gives you this behavior for controller methods by default.

---

# 25. Content-Type

`Content-Type` tells the server:

> What format is the data I'm sending?

Example:

```http
Content-Type: application/json
```

means:

```text
Request body = JSON
```

Spring can specify it:

```java
@PostMapping(
    value = "/medicines",
    consumes = "application/json"
)
public Medicine addMedicine(
        @RequestBody Medicine medicine) {
    // ...
}
```

---

# 26. Accept

`Accept` tells the server:

> What response format does the client want?

Example:

```http
Accept: application/json
```

Spring can specify:

```java
@GetMapping(
    value = "/medicines",
    produces = "application/json"
)
public List<Medicine> getMedicines() {
    // ...
}
```

So remember:

```text
Content-Type
     ↓
"What am I sending?"

Accept
     ↓
"What response format do I want?"
```

---

# 27. `consumes` and `produces`

These are important attributes of mapping annotations.

### `consumes`

Controls what request content type the endpoint accepts.

```java
@PostMapping(
    value = "/medicines",
    consumes = "application/json"
)
```

### `produces`

Controls what response content type the endpoint produces.

```java
@GetMapping(
    value = "/medicines",
    produces = "application/json"
)
```

Mental model:

```text
consumes → Request
produces → Response
```

---

# 28. Complete REST Controller Example

This combines the major HTTP concepts:

```java
@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @GetMapping
    public ResponseEntity<List<Medicine>> getAll() {

        List<Medicine> medicines =
                medicineService.getAll();

        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getOne(
            @PathVariable Long id) {

        Medicine medicine =
                medicineService.getMedicine(id);

        return ResponseEntity.ok(medicine);
    }

    @PostMapping
    public ResponseEntity<Medicine> add(
            @RequestBody Medicine medicine) {

        Medicine saved =
                medicineService.addMedicine(medicine);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(
            @PathVariable Long id,
            @RequestBody Medicine medicine) {

        Medicine updated =
                medicineService.update(id, medicine);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        medicineService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
```

---

# 29. Important Spring HTTP/REST Annotations

Keep this table for revision:

| Annotation              | Purpose                                 |
| ----------------------- | --------------------------------------- |
| `@RestController`       | Creates REST controller                 |
| `@Controller`           | Creates MVC controller                  |
| `@RequestMapping`       | Maps HTTP requests to controller/method |
| `@GetMapping`           | Handles GET                             |
| `@PostMapping`          | Handles POST                            |
| `@PutMapping`           | Handles PUT                             |
| `@PatchMapping`         | Handles PATCH                           |
| `@DeleteMapping`        | Handles DELETE                          |
| `@PathVariable`         | Gets value from URL path                |
| `@RequestParam`         | Gets query parameter                    |
| `@RequestBody`          | Gets data from request body             |
| `@RequestHeader`        | Gets HTTP header                        |
| `@CookieValue`          | Gets cookie value                       |
| `@ResponseBody`         | Places return value in response body    |
| `@ResponseStatus`       | Specifies HTTP status                   |
| `@ExceptionHandler`     | Handles exceptions                      |
| `@RestControllerAdvice` | Global REST exception handling          |
| `@Valid`                | Validates request body                  |
| `@CrossOrigin`          | Configures CORS                         |

---

# 30. The Most Important Mental Model

When a client sends:

```http
POST /medicines/101
Content-Type: application/json

{
    "name": "Paracetamol",
    "quantity": 20
}
```

Spring roughly processes it like:

```text
                HTTP REQUEST
                     │
                     ↓
             ┌───────────────┐
             │   Controller  │
             └───────┬───────┘
                     │
             @PostMapping
                     │
                     ↓
              @PathVariable
                   101
                     │
                     ↓
               @RequestBody
                     │
                     ↓
              JSON → Java
                     │
                     ↓
                 Service
                     │
                     ↓
                Repository
                     │
                     ↓
                 Database
                     │
                     ↓
               Java Object
                     │
                     ↓
              Java → JSON
                     │
                     ↓
              HTTP RESPONSE
                     │
              201 Created
                     +
                 JSON body
```

## What you should learn first

Don't memorize all the annotations at once. Master these **10 first**:

```text
1. HTTP methods
2. HTTP status codes
3. HTTP request
4. HTTP response
5. @RestController
6. @RequestMapping
7. @GetMapping / @PostMapping / @PutMapping / @DeleteMapping
8. @PathVariable
9. @RequestParam
10. @RequestBody
```

Then move to:

```text
11. ResponseEntity
12. @RequestHeader
13. Content-Type / Accept
14. consumes / produces
15. @ResponseStatus
16. Exception handling
17. Validation
18. DTOs
19. CORS
20. Pagination
```

Once these are clear, you'll have the **HTTP foundation needed to build Spring Boot REST APIs**, rather than just memorizing annotations.

    HTTP REQUEST
    │
    │
    ↓
    ┌──────────────────────┐
    │   DispatcherServlet  │
    │   (Front Controller) │
    └──────────┬───────────┘
    │
    │ "Which Controller
    │  should handle this?"
    ↓
    ┌──────────────────────┐
    │    HandlerMapping    │
    └──────────┬───────────┘
    │
    │ Finds matching
    │ Controller + Method
    ↓
    ┌──────────────────────┐
    │     Controller       │
    │  @RestController    │
    └──────────┬───────────┘
    │
    @PostMapping
    │
    ↓
    @PathVariable
    101
    │
    ↓
    @RequestBody
    │
    ↓
    JSON → Java
    │
    ↓
    Service
    │
    ↓
    Repository
    │
    ↓
    Database
    │
    ↓
    Java Object
    │
    ↓
    Java → JSON
    │
    ↓
    ┌──────────────────────┐
    │  DispatcherServlet   │
    │  sends HTTP response │
    └──────────┬───────────┘
    │
    ↓
    HTTP RESPONSE
    │
    201 Created
    +
    JSON Body

What does each part do?
1. HTTP Request
   Postman sends something like:
   POST /medicines/101
   Content-Type: application/json

{
"name": "Paracetamol",
"quantity": 20
}
The request first enters the Spring application.
2. 
         DispatcherServlet
         
         DispatcherServlet is the front controller of Spring MVC.
            Think of it as the main entry point for incoming HTTP requests.
            HTTP Request
               ↓
            DispatcherServlet
            It receives the request and asks:
            "Which controller/method should handle this request?"

   3. HandlerMapping
   
            HandlerMapping helps DispatcherServlet find the appropriate controller and method.
               For example, Spring has:
               @PostMapping("/medicines/{id}")
               public Medicine update( @PathVariable Long id,
                                       @RequestBody Medicine medicine)
               {   ...      }
   
                  Request:
                  POST /medicines/101
                  HandlerMapping helps determine:
                  POST /medicines/101
                        ↓
                  MedicineController.update()
                  So its basic job is:
                  Request → Find matching handler
  