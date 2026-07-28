package org.Spring.core;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class LifecycleApplication {
        public static void main(String[] args) {
            SpringApplication.run(LifecycleApplication.class, args);
        }
    }

/*
* // Startup order for every non-lazy bean:
setBeanName()           ← BeanNameAware (told its name)
setApplicationContext() ← ApplicationContextAware (given the container)
Constructor             ← object created
afterPropertiesSet()    ← InitializingBean (if implemented)
@PostConstruct          ← annotation-based init

// On app shutdown (Ctrl+C):
@PreDestroy             ← annotation-based cleanup
destroy()               ← DisposableBean (if implemented)
*
* */