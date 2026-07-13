package org.Spring.core.learn.controller;


import org.Spring.core.learn.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController   //This tells Spring that these class will accept the Https requests from web and handle these and return the response.    RestController is Child of Controller Annotation.
public class TestController {

    @Autowired
    private MessageService messageService;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping("/test")
    public String getMessage(){
        return messageService.returnMeassage() + " | App Version"+ appVersion;
    }

}


/*
Small Refinement
@RestController is actually two annotations combined:
java@RestController
=
@Controller + @ResponseBody
PartMeaning@ControllerTells Spring Container: "This class handles incoming HTTP requests"@ResponseBodyTells Spring Container: "Return the response directly as data (JSON/text), not as a HTML page"

Simple Example to understand the difference
java@Controller          // returns a HTML page (like a website)
public class WebController {
    @GetMapping("/home")
    public String home() {
        return "home.html";   // ← returns a VIEW (html page)
    }
}
java@RestController      // returns raw data (JSON or text)
public class ApiController {
    @GetMapping("/home")
    public String home() {
        return "Hello!";   // ← returns DATA directly
    }
}

Your Refined Definition

@RestController tells the Spring Container: "This class handles incoming HTTP requests and returns the response directly as data (JSON/text), not as an HTML page."

One Line to Remember

@Controller = for websites (returns HTML) | @RestController = for REST APIs (returns JSON/text)


Yes, that's correct! `@RequestBody` takes the incoming JSON from the request and converts it into a Java object (so you can use it directly in your method).

| @RequestBody                  |                        @ResponseBody |
| --- | --- |
| Used on a method **parameter**                |       Used on a method (or implied by `@RestController`) |
| Converts incoming **JSON → Java Object**      |       Converts outgoing **Java Object → JSON** |
| Used when **client sends data** (POST/PUT)    |       Used when **server sends data back** |
| Example: `@RequestBody User user`             |       Example: `return user;` → sent as JSON |
| Direction: **Request → Java**                 |       Direction: **Java → Response** |
|Needs Jackson library to deserialize JSON into object| Needs Jackson library to serialize object into JSON |

### Quick Example

```java
@PostMapping("/users")
@ResponseBody                          // converts returned User object → JSON
public User createUser(@RequestBody User user) {   // converts incoming JSON → User object
    System.out.println(user.getName());
    return user;
}
```

> **Note:** If your class is annotated with `@RestController`, you don't need to add `@ResponseBody` separately on every method — it's already included automatically (`@RestController` = `@Controller` + `@ResponseBody`).

* */