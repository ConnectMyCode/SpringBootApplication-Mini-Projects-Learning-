package org.source.learn.beans;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("singleton")  //  DEFAULT scope — ONE object for the whole app
public class SingletonBean {

    private final String id = UUID.randomUUID().toString();

    public SingletonBean(){
        System.out.println("SingletonBean CREATED: "+ id);
    }

    public String getId(){
        return id;
    }

}
