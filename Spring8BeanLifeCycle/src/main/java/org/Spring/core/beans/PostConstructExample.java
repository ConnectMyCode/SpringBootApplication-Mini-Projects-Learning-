package org.Spring.core.beans;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PostConstructExample {

    public PostConstructExample()
    {
        System.out.println("1. PostConstructBean — Constructor called");
        // at this point @Value fields are still null — too early!);
    }

    @PostConstruct
    // Called by Spring AFTER constructor AND after all @Autowired/@Value injections are done
    public void init() {
        System.out.println("2. PostConstructBean — @PostConstruct called, all fields are ready now");
    }

    public String status() {
        return "@PostConstruct ran after constructor and injection";
    }

}
