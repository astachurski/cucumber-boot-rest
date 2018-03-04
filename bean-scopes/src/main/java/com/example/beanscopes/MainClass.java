package com.example.beanscopes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//1
//@Component

//2
@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainClass {

    @Autowired

    private Dependency dependency;

    public void hello(){
        System.out.println("hello from : " + this.getClass() + " / " + this.toString());

        dependency.hello();
    }
}
