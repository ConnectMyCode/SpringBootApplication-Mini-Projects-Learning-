package org.Spring.core.beans;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class DisposableBeanInterface implements DisposableBean {

public DisposableBeanInterface(){    }

    @Override
    public void destroy(){
        // Spring calls this on shutdown — same timing as @PreDestroy
        System.out.println("DisposableBeanExample — destroy() called! Cleaning up...");
    }

    public String status() {
        return "I will clean up on shutdown via DisposableBean.destroy()";
    }

}
