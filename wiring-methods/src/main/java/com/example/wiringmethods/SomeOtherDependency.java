package com.example.wiringmethods;

import org.springframework.stereotype.Component;

@Component
public class SomeOtherDependency {

    public void hello(){
        System.out.println(this.getClass() +  "     hello!");
    }
}
