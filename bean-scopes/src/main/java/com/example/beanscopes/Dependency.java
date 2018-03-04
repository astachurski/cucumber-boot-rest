package com.example.beanscopes;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
//4
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)

//5 - for case with MainClass as singleton and this as prototype we need this
// - this way Spring does not return "snapshot" of all dependency chain but rather a proxy object which
// when called - creates and returns new instance of target class
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Dependency {
    public void hello(){
        System.out.println("hello from : " + this.getClass() + " / " + this.toString());
    }
}
