package org.Spring.core.learn.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

// THEORY: ApplicationRunner is the modern interface from Spring Boot v2.x
// THEORY: Preferred over CommandLineRunner because it gets ApplicationArguments
//         which can distinguish between option and non-option arguments
// THEORY: @Order(1) means this runs FIRST before JobLoadRunner

@Component
@Order(1)  // THEORY: runs first because order value is lower
public class CacheLoadRunner implements ApplicationRunner {

    // THEORY: ApplicationRunner's abstract method — receives ApplicationArguments
    // instead of raw String[], giving us more power to inspect arguments
    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("=== CacheLoadRunner START ===");

        // THEORY: Simulating "load data from database into cache" — the real use case
        // mentioned in theory: load data at startup so app runs faster
        System.out.println("Loading users from database into cache...");
        System.out.println("Cache entry 1 → User: John,  Role: ADMIN");
        System.out.println("Cache entry 2 → User: Alice, Role: USER");
        System.out.println("Cache entry 3 → User: Bob,   Role: MANAGER");
        System.out.println("Cache loaded successfully! App will now serve faster.");

        // THEORY: Accessing NON-OPTION arguments (unnamed args)
        // Example command: java -jar app.jar input.txt output.txt
        //                                     --------  ---------
        //                                     non-option non-option
        System.out.println("\n--- Non-Option Arguments ---");
        List<String> nonOptionArgs = args.getNonOptionArgs();
        if (nonOptionArgs.isEmpty()) {
            System.out.println("No non-option arguments provided");
        } else {
            nonOptionArgs.forEach(arg -> System.out.println("Non-option arg: " + arg));
        }

        // THEORY: Accessing OPTION arguments (named args starting with --)
        // Example command: java -jar app.jar --server.port=9090 --db.user=admin
        //                                     ----------------   ------------
        //                                     option arg         option arg
        System.out.println("\n--- Option Arguments ---");
        Set<String> optionNames = args.getOptionNames();
        if (optionNames.isEmpty()) {
            System.out.println("No option arguments provided");
        } else {
            for (String optionName : optionNames) {
                List<String> values = args.getOptionValues(optionName);
                System.out.println("Option name  : " + optionName);
                System.out.println("Option value : " + values);
                System.out.println("---");
            }
        }

        System.out.println("=== CacheLoadRunner END ===");
    }
}