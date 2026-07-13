package org.source.learn.beans;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("prototype")   //Every Time a Object of class  PrototypeBean is created a New bean Object is created
public class PrototypeBean {

    private final String id = UUID.randomUUID().toString();

    public PrototypeBean() {
        System.out.println("PrototypeBean CREATED: " + id);
    }

    public String getId(){
        return id;
    }



}
