package org.Spring.core.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class,args);
    }

}


/*

## Putting both halves together

**Startup phase** (happens once, before any request):
1. `main()` calls `SpringApplication.run()`
2. The Spring Container (`ApplicationContext`) is created
3. `@ComponentScan` scans your packages
4. Every `@Component`, `@RestController`, `@Configuration` class gets turned into a bean and stored in the container
5. Wherever a bean needs another bean (`@Autowired`), the container wires them together
6. `@PropertySource` + `@Value` happen here too — values get pulled from `.properties` files and stuffed into fields

**Request phase** (happens every time a request comes in):
1. Browser sends an HTTP request
2. Embedded Tomcat (bundled inside Spring Boot) receives it
3. `DispatcherServlet` is the single entry point — it routes the request based on the URL
4. It matches the URL to your `@GetMapping` method and fetches the corresponding controller bean (no creation, it's reused)
5. Your method runs using already-injected beans
6. `@ResponseBody` (part of `@RestController`) converts your return value into the HTTP response body
7. Response travels back through Tomcat to the browser

One-line mental model: **annotations are just metadata** — they don't do anything by themselves. The Spring Container is the thing that actually *reads* those annotations at startup and acts on them. By the time a request arrives, all the "magic" has already happened; the request just walks through a pre-built object graph.


* */