package org.source.learn.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "application", proxyMode = ScopedProxyMode.TARGET_CLASS)
// ONE object per ServletContext (basically same as singleton in most apps)
public class ApplicationBean {
    private final String id = UUID.randomUUID().toString();

    public ApplicationBean() {
        System.out.println("ApplicationBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}
