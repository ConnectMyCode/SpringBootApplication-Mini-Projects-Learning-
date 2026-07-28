package org.Spring.core.learn.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// THEORY: You can have as many runner components as you want
// THEORY: @Order(3) — this runs last, after both other runners
// THEORY: "ApplicationArguments class object can be accessed at ANY place"
//          — meaning any runner or even any @Component can @Autowired ApplicationArguments

@Component
@Order(3)
public class ArgumentInspectorRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("=== ArgumentInspectorRunner START ===");

        // THEORY: This demonstrates the full example from the theory notes:
        // java -jar app.jar Job-details.txt --server.port=9797 Weekly
        //                   ---------------  ----------------  ------
        //                   non-option       option            non-option

        System.out.println("Total raw args    : " + args.getSourceArgs().length);
        System.out.println("Non-option count  : " + args.getNonOptionArgs().size());
        System.out.println("Option count      : " + args.getOptionNames().size());

        // Checking a specific option argument
        // THEORY: option args are used for "configuration properties"
        if (args.containsOption("server.port")) {
            System.out.println("Custom port provided: " + args.getOptionValues("server.port"));
        } else {
            System.out.println("No custom port — using default from application.properties");
        }

        System.out.println("=== ArgumentInspectorRunner END ===");
    }
}


