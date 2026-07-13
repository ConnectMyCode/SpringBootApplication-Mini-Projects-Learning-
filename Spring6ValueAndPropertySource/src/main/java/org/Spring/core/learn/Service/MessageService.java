package org.Spring.core.learn.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    @Value("${app.name}")
    private String appName;

    @Value("${app.message}")
    private String appMessage;

    public String returnMeassage()
    {
        return "App Name :" +appName + "\n App Message : "+appMessage;
    }
}
