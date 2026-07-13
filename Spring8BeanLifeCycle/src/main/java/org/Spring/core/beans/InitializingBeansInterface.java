package org.Spring.core.beans;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class InitializingBeansInterface implements InitializingBean {

   public InitializingBeansInterface()
   {
       System.out.println("1. InitializingBeanInterface - Constructor called");
   }


    @Override
    public void afterPropertiesSet() throws Exception {
        // Spring calls this automatically after all injections are done
        // Same timing as @PostConstruct but done via interface
        System.out.println("2. InitializingBeanExample — afterPropertiesSet() called");
    }

    public String status()
    {
        return "afterPropertiesSet() ran after constructor and injection";
    }

}