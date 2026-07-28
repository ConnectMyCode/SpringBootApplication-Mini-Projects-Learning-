package org.source.learn.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
// new object PER USER SESSION (same browser tab/session = same object)
public class SessionBean {
    private final String id = UUID.randomUUID().toString();

    public SessionBean() {
        System.out.println("SessionBean CREATED: " + id);
    }

    public String getId() {
        return id;
    }
}