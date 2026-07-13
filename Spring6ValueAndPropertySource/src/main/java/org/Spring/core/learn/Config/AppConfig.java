package org.Spring.core.learn.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration  //Tells the SPring container to treat this as a configuration Class which contains the Settings/Config
@PropertySource("classpath:application.properties") //It tells the Spring Container to load "Application.properties" file... If there are multiple files [present in the project we use PropertySource to specify which file to load.
@ComponentScan("org.Spring.core.learn") //ComponetScane: -> It tells Spring Contiainer to Start looking for Spring annotations from the mentioned package and scan its sub packages for annotations so can register them as spring beans in container
public class AppConfig {


    // // No code needed here — just the annotations do the work!


}
/*

Great question! Let me explain it simply.

---

## Without Config Class — Spring Boot does everything automatically

In Spring Boot, `@SpringBootApplication` already includes:
- `@ComponentScan` — auto scans your package
- Auto reads `application.properties` — no need for `@PropertySource`

So honestly, **in Spring Boot you rarely need a Config class.**

---

## Then WHY does Config class exist?

### Real Purpose — When you need to **customize** things

---

### Use Case 1 — Load a DIFFERENT properties file
```java
@Configuration
@PropertySource("classpath:mydb.properties")  // ← custom file, not application.properties
public class AppConfig {
}
```
```properties
# mydb.properties
db.url=jdbc:mysql://localhost:3306/mydb
db.username=root
```
> Spring Boot won't auto-read `mydb.properties` — you need `@PropertySource` for this.

---

### Use Case 2 — Create objects (Beans) of classes you DON'T own
```java
@Configuration
public class AppConfig {

    // You can't put @Component on a 3rd party class
    // So you manually create the object here
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();  // Jackson library class
    }
}
```
> `@Bean` inside `@Configuration` = manually register an object with Spring.

---

### Use Case 3 — Scan a DIFFERENT package
```java
@Configuration
@ComponentScan("com.different.package")  // scan outside your main package
public class AppConfig {
}
```

---

## Simple Rule to remember

| Situation | Need Config Class? |
|---|---|
| Normal Spring Boot project | ❌ No — auto handled |
| Loading a custom `.properties` file | ✅ Yes — use `@PropertySource` |
| Creating beans from 3rd party classes | ✅ Yes — use `@Bean` |
| Scanning a different package | ✅ Yes — use `@ComponentScan` |

---

## One Line Summary

> **Config class = a place to manually tell Spring things it can't figure out automatically.**

In Spring Boot, think of it as an **override / customization class** — you only write it when the defaults aren't enough.

* */