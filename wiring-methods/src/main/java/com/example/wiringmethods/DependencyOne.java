package com.example.wiringmethods;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DependencyOne implements DependencyContract {

    public void sayHello(){
        System.out.println("hello from " + this.getClass());
    }
}
