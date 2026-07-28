package org.Spring.core.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextAwareAndBeanNameAwareBean implements ApplicationContextAware, BeanNameAware
{
    private ApplicationContext applicationContext;
    private String beanName;

    //Called by Spring - Injects the container itself into this bean
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException
    {
           this.applicationContext = ctx;
        System.out.println("setApplicationContext() called -- I now have access to the whole container! ");
    }

    // Called by Spring — tells this bean what name it was registered as
    @Override
    public void setBeanName(String name){
        this.beanName = name;
        System.out.println("setBeanName() called — my bean name is: " + name);
    }

    public String status()
    {           // Using ApplicationContext to fetch another bean dynamically
        EagerBean eagerBean = (EagerBean) applicationContext.getBean("eagerBean");

        return "My Bean Name  : "+ beanName  +
                                 " | EagerBean fetched from context :" + eagerBean.greet() ;
    }



}
