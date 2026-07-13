package org.Spring.core.beans;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class PreDestroyBean {

    public PreDestroyBean(){
        System.out.println("PreDestroyBean - Created");
    }


    @PreDestroy
    // Called by Spring JUST BEFORE this bean is removed from the container (app shutdown)
    public void cleanUp(){
        // Real use: close DB connections, release file handles, stop threads
        System.out.println("PreDestroyBean — @PreDestroy cleanup! Closing resources...");
    }

    public String status() {
        return "I will clean up on shutdown via @PreDestroy";
    }


}
