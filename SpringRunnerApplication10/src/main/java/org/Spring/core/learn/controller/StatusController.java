package org.Spring.core.learn.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {

    // THEORY: "You can access option and non-option arguments at ANY place
    //          through ApplicationArguments class object"
    // This proves that — we're accessing args in a Controller, not just in runners!
    @Autowired
    private ApplicationArguments args;

    @GetMapping("/status")
    public String status() {
        return "App is running! Runners executed at startup. Hit /args to see arguments.";
    }

    @GetMapping("/args")
    public String showArgs() {
        // THEORY: Accessing ApplicationArguments outside of runners — works anywhere!
        List<String> nonOption = args.getNonOptionArgs();
        return "Non-option args: " + nonOption +
                " | Option args: "  + args.getOptionNames();
    }
}