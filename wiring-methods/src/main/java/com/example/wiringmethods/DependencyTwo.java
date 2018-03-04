package com.example.wiringmethods;

import org.springframework.stereotype.Component;

@Component
public class DependencyTwo implements DependencyContract {

    public void sayHello(){
        System.out.println("hello from " + this.getClass());
    }
}
