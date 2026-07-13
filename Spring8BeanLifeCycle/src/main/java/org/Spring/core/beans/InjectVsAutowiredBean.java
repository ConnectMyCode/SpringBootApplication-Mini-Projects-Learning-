package org.Spring.core.beans;

import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InjectVsAutowiredBean {


    // @Autowired — Spring's own annotation
    @Autowired
    private EagerBean eagerBean;

    @Inject
    // @Inject — Jakarta (Java standard) annotation, Spring supports it too
    private PreDestroyBean preDestroyBean;

    public String status(){
        return "Autowired: " + eagerBean.greet() +
                " | Inject: "  + preDestroyBean.status();
    }


}
