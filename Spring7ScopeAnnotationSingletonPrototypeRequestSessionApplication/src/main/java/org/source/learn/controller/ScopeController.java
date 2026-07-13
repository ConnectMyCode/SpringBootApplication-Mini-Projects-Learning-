package org.source.learn.controller;

import org.source.learn.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScopeController {

    @Autowired private SingletonBean singletonBean;
    @Autowired private PrototypeBean prototypeBean;
    @Autowired private RequestBean requestBean;
    @Autowired private SessionBean sessionBean;
    @Autowired private ApplicationBean applicationBean;

    @Autowired private org.springframework.context.ApplicationContext context;
    //Injecting and Loading the Spring Container  so I can Access the Beans from container here...


    @GetMapping("/scopes")
    public String checkScopes() {
        return "Singleton:   " + singletonBean.getId()   + "\n" +
                "Prototype:   " + prototypeBean.getId()   + "\n" +
                "Request:     " + requestBean.getId()     + "\n" +
                "Session:     " + sessionBean.getId()     + "\n" +
                "Application: " + applicationBean.getId();
    }


    @GetMapping("/prototype")
    public String testPrototype(){

        PrototypeBean p1 = (PrototypeBean)context.getBean("prototypeBean");     //Remember :-> Spring Container stores a Spring Bean Object in Camel Case of The Class Name First word Small
        PrototypeBean p2 = (PrototypeBean)context.getBean("prototypeBean");
        return
                "P1: "+ "id: "+p1.getId() +" \n "+
                "P2: "+ "id: "+p2.getId();

    }

}


/*Response :->

GET http://localhost:8080/scopes

HTTP/1.1 200
Set-Cookie: JSESSIONID=41DCBA30370E15AC240A13C0D1710007; Path=/; HttpOnly
Content-Type: text/plain;charset=UTF-8
Content-Length: 249
Date: Tue, 30 Jun 2026 13:15:29 GMT

Singleton:   2005e7f3-c06a-4e68-8d5e-bf065c5c9c74
Prototype:   98731599-68d8-45b6-9585-6a2b80a23cd7
Request:     394f042d-778d-4e98-b96d-cfbf12f64807
Session:     42117634-11f4-440c-9897-c96f93b79cf9
Application: aabdaa16-17d7-4333-b675-f8986235dc7d


Singleton : Once registered in the Spring Container, Each time  oBject is created the same Bean is referenced ... No new bean is created.


//Application Bean in Spring Container  will be referneced every time through out the end Only if we restart the Application then it is registered and Initialized again as A New Object...
Application: 9f456001-ab50-4c43-ad38-a05ab9f0cb8f


//Prortotype   WHen a Bean Object is called of type Prototype a new bean is Inititialized  and passed by Spring Container but it is not registered or saved in contianer.   Lazy initialization:-> A bean is created when needed and not at the Start of Spring Continaer Scanning for objects.
P1: id: 39b7272e-6855-4c97-9af2-3cd6081ab352
P2: id: fd2e84dd-3198-4315-9f36-12693b707af3


//Request     //Each
New Request:     4fea5bd0-0c8b-480a-aabc-13a68ad3ec63   //Id changed
New Request:     1ebdb04b-3493-4f81-b300-8d673da51a2d


Here's a mini project that lets you **see** all 5 scopes in action by hitting endpoints and watching object IDs change (or not change).

## Project Structure
```
src/main/java/com/example/scope/
├── ScopeDemoApplication.java
├── beans/
│   ├── SingletonBean.java
│   ├── PrototypeBean.java
│   ├── RequestBean.java
│   ├── SessionBean.java
│   └── ApplicationBean.java
└── controller/
    └── ScopeController.java
```

---

## Core idea before the code

Every bean below does the **same thing** — it generates a random ID when created (`new` is called) and exposes it via a `getId()` method. We just watch **when** that ID changes.

---

## 1. `pom.xml` (add this dependency)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## 2. `SingletonBean.java`
```java
package com.example.scope.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope("singleton")   // DEFAULT scope — ONE object for the whole app
public class SingletonBean {
    private final String id = UUID.randomUUID().toString();

    public SingletonBean() {
        System.out.println("SingletonBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}
```

> **Notice the constructor print statement** — it tells us exactly when the object is created. With singleton, you'll only see this print **once** in your console, ever.

---

## 3. `PrototypeBean.java`
```java
package com.example.scope.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope("prototype")   // a NEW object every time it's requested
public class PrototypeBean {
    private final String id = UUID.randomUUID().toString();

    public PrototypeBean() {
        System.out.println("PrototypeBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}
```

---

## 4. `RequestBean.java`
```java
package com.example.scope.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
// new object PER HTTP REQUEST, same object reused within ONE request
public class RequestBean {
    private final String id = UUID.randomUUID().toString();

    public RequestBean() {
        System.out.println("RequestBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}
```

> **Why `proxyMode = ScopedProxyMode.TARGET_CLASS`?** Request/Session/Application scoped beans need a "proxy" placeholder because they can't be created at startup (there's no HTTP request yet!). Spring injects a proxy that fetches/creates the real object only when a request actually comes in.

---

## 5. `SessionBean.java`
```java
package com.example.scope.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
// new object PER USER SESSION (same browser tab/session = same object)
public class SessionBean {
    private final String id = UUID.randomUUID().toString();

    public SessionBean() {
        System.out.println("SessionBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}
```

---

## 6. `ApplicationBean.java`
```java
package com.example.scope.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope(value = "application", proxyMode = ScopedProxyMode.TARGET_CLASS)
// ONE object per ServletContext (basically same as singleton in most apps)
public class ApplicationBean {
    private final String id = UUID.randomUUID().toString();

    public ApplicationBean() {
        System.out.println("ApplicationBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}
```

---

## 7. `ScopeController.java` (the part you'll actually test)
```java
package com.example.scope.controller;

import com.example.scope.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScopeController {

    @Autowired private SingletonBean singletonBean;
    @Autowired private PrototypeBean prototypeBean;
    @Autowired private RequestBean requestBean;
    @Autowired private SessionBean sessionBean;
    @Autowired private ApplicationBean applicationBean;

    @GetMapping("/scopes")
    public String checkScopes() {
        return "Singleton:   " + singletonBean.getId()   + "\n" +
               "Prototype:   " + prototypeBean.getId()   + "\n" +
               "Request:     " + requestBean.getId()     + "\n" +
               "Session:     " + sessionBean.getId()     + "\n" +
               "Application: " + applicationBean.getId();
    }
}
```

---

## 8. `ScopeDemoApplication.java`
```java
package com.example.scope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScopeDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScopeDemoApplication.class, args);
    }
}
```

---

## How to test — hit `/scopes` multiple times

Go to `http://localhost:8080/scopes` and **refresh it 3-4 times.** Watch the IDs.

| Scope             | What you'll observe |
|---|---|
| **Singleton**     | Same ID every single time, forever |
| **Prototype**     | Same ID across refreshes — *unless* injected fresh per request (see note below) |
| **Request**       | New ID on every refresh (new HTTP request = new bean) |
| **Session**       | Same ID across refreshes — **until** you open an incognito window (new session = new ID) |
| **Application**   | Same ID always — behaves like singleton |

---

## ⚠️ Important Prototype Gotcha

You'll notice **Prototype behaves like Singleton** in this setup! That's a classic beginner trap.

**Why:** `ScopeController` is itself a singleton — created **once**. `@Autowired PrototypeBean` is injected **once**, at controller creation time. So you only ever see ONE prototype object, even though the scope says "create new every time."

### To truly test Prototype scope, fetch it manually using `ApplicationContext`:
```java
@Autowired
private org.springframework.context.ApplicationContext context;

@GetMapping("/prototype-test")
public String testPrototype() {
    PrototypeBean p1 = context.getBean(PrototypeBean.class);
    PrototypeBean p2 = context.getBean(PrototypeBean.class);
    return "First call:  " + p1.getId() + "\n" +
           "Second call: " + p2.getId();
}
```
Hit `/prototype-test` — now you'll see **two different IDs** in the **same response**, proving a new object was created each time you called `getBean()`.

---

## Summary Table

| Scope                         | New object created...                 | Real-world use case |
|---|---|---|
| `singleton` (default)         | Once,for the whole app                | Services, repositories — shared logic |
| `prototype`                   | Every time `getBean()` is called      | Objects that hold temporary/mutable state |
| `request`                     | Once per HTTP request                 | Holding request-specific data (e.g. request ID, tracing) |
| `session`                     | Once per user session                 | Shopping cart, logged-in user data |
| `application`                 | Once per ServletContext               | App-wide config, almost same as singleton |

> **Tip:** `request`, `session`, and `application` scopes only work in a **web application context** — they'll throw an error if used outside a web app (e.g. in a plain Java/CLI Spring project).


* */


