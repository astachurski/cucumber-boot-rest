package com.example.configurationhandling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "bean.settings")
public class BeanToConfigureSecond {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    private int age;

    public void setAge(int age) {
        this.age = age;
    }

    public void hello() {
        System.out.println("hello from: " + this.getClass());

        System.out.println(" ---- name is: " + this.name);
        System.out.println(" ---- age is: " + this.age);

    }
}
