package org.Spring.core.contoller;

import jakarta.annotation.PreDestroy;
import org.Spring.core.beans.*;
import org.Spring.core.dto.UserDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LifeCycleController {

   @Autowired private LazyBean lazyBean;   // 1st Bean Here LAzyBean is required so Container will create the BEan Now .
   //If the above line is Commented see the O/p will see the change because the Bean is not created as it is not required not even at start of the Container.
    @Autowired private EagerBean eagerBean;

    @Autowired ApplicationContext ctx;

    @Autowired InitializingBeansInterface initializingBeans;

    @Autowired PostConstructExample postConstruct;

    @Autowired PreDestroyBean preDestroyBean;

    @Autowired DisposableBeanInterface disposableBeanInterface;

    @Autowired ApplicationContextAwareAndBeanNameAwareBean applicationContextAwareAndBeanNameAwareBean;

    @GetMapping("/dto")
    public UserDTO dto(@RequestBody UserDTO userDTO) {
        // DTO created manually — not a Spring bean
        UserDTO user = new UserDTO("John", "john@email.com", "ADMIN");
        return user;
    }

    @GetMapping("/lazy")
    public LazyBean lazy() {
        // LazyBean is created HERE for the first time (check console)
        LazyBean lzy = (LazyBean)ctx.getBean("lazyBean");  //A. 2nd Bean
       String mssg =  lzy.greet();
        System.out.println(mssg);
        return (LazyBean)ctx.getBean("lazyBean"); //3rd Bean
    }

    @GetMapping("/eager")
    public String eager() {
        return eagerBean.greet();
    }


    @GetMapping("/postConstructExample")
    public String postConstructExample(){
        return postConstruct.status();
    }

    @GetMapping("/initializingBeansInterface")
    public String initializingBeansInterface()
    {
         return initializingBeans.status();
    }

    @GetMapping("/preDestroy")
    public String preDestroy(){return preDestroyBean.status(); }

    @GetMapping("/disposableBeanInterface")
    public String disposableBeanInterface(){ return disposableBeanInterface.status();}

    @GetMapping("/applicationContextAwareAndBeanNameAwareBean")
    public String ApplicationContextAwareAndBeanNameAwareBean(){
       return  applicationContextAwareAndBeanNameAwareBean.status() ;
    }


}

