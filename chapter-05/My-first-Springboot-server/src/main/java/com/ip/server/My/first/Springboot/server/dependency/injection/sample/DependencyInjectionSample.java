package com.ip.server.My.first.Springboot.server.dependency.injection.sample;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DependencyInjectionSample {
    private final ApplicationContext applicationContext;

    public DependencyInjectionSample(
        ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing MyComponent");
        for (String beanDefinitionName :
            applicationContext
                .getBeanDefinitionNames()) {

            System.out.println("Bean Name: "
                    + beanDefinitionName);
        }
    }
}
