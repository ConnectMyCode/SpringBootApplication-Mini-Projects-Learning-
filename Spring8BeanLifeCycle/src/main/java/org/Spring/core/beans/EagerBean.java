package org.Spring.core.beans;

import org.springframework.stereotype.Component;

@Component
public class EagerBean {

    public EagerBean()
    {
        System.out.println("EagerBean Created at Startup - eager (default)");
    }

    public String greet(){
        return "I was created at Startup!!!";    }
}




