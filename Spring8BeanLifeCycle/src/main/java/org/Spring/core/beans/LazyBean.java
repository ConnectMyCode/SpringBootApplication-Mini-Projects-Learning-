package org.Spring.core.beans;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy       // NOT created at startup — created only when first requested
public class LazyBean {
    public LazyBean() {
        System.out.println("LazyBean CREATED — only because someone asked for me!");
    }

    public String greet() {
        return "I was created lazily — only when first needed!";
    }

}
/*
Watch your console: When the app starts, you'll see EagerBean CREATED print immediately.
LazyBean CREATED will only print the first time you hit /lazy endpoint — not at startup.
* */
