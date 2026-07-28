package org.Spring.core.learn.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// THEORY: CommandLineRunner is the legacy interface from Spring Boot v1.x
// THEORY: @Component makes this a Spring Bean so Spring Boot can find it in the container
// THEORY: Spring Boot searches for runner components at startup and executes them automatically
// THEORY: @Order(2) means this runs SECOND — after JobLoadRunner which has @Order(1)

@Component
@Order(2)  // THEORY: controls execution order when multiple runners exist
public class JobLoadRunner implements CommandLineRunner {

    // THEORY: CommandLineRunner's abstract method — receives raw String args from command line
    // THEORY: This is a one-time execution task — runs once at startup, never again
    @Override
    public void run(String... args) throws Exception {

        System.out.println("=== JobLoadRunner START ===");

        // THEORY: Simulating "load job schedules from database" — the real use case
        // mentioned in theory notes
        System.out.println("Loading job schedules from database...");
        System.out.println("Job 1: Daily Report   — scheduled at 08:00 AM");
        System.out.println("Job 2: Weekly Backup  — scheduled at Sunday 11:00 PM");
        System.out.println("Job 3: Monthly Audit  — scheduled at 1st of every month");
        System.out.println("Job schedules loaded successfully!");

        // THEORY: args here are raw strings — CommandLineRunner cannot distinguish
        // between option and non-option arguments (that's why ApplicationRunner is better)
        System.out.println("Raw args received: " + args.length);
        for (String arg : args) {
            System.out.println("  arg: " + arg);
        }

        System.out.println("=== JobLoadRunner END ===");
    }
}