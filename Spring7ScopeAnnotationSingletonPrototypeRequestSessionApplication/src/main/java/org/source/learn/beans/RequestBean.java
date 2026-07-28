package org.source.learn.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
// new object PER HTTP REQUEST, same object reused within ONE request
public class RequestBean {
    private final String id = UUID.randomUUID().toString();

    public RequestBean() {
        System.out.println("RequestBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }

}