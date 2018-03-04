package com.example.componentscan.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainBean {

    @Autowired
    private DependencyBean dependencyBean;

    public void useDependency(){
        dependencyBean.hello();
    }
}
