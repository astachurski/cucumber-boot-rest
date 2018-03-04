package com.example.configurationhandling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "other.persons")
public class BeanToConfigure {

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;

    //4
    @Value("environments.dev.url")
    private String url;

    //4 - we can also store lists - but we need  to provide SETTER for
    //configuration to inject values - List<String> instance with data
    //will be provided automatically
    @Value("names")
    private List<String> names;

    public void setNames(List<String> names) {
        this.names = names;
    }
    //5


    public void hello(){
        System.out.println("hello from: " + this.getClass());
        System.out.println("----- name : " + name);
        System.out.println("----  age : " + age);

        //4
        System.out.println("---- url: " + url);

        if (names != null){
            System.out.println(" ---- names from list :" );
            for (String s : names) {
                System.out.println(s);
            }

        }

    }
}
