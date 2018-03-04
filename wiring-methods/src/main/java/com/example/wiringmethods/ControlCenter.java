package com.example.wiringmethods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//1 show simple injection to field - whatever name can be used (Type counts)
//2 use interface instead - name MATTERS! implementation is chosen by Name
//3 uncomment 3 and show error message - can't use by Name with interface!
//4 but use @Primary or @Qualifier as in previous demonstation to resolve to issue!
//5 Injection by constructor
//6 with Injectioin by constructor - we can inject implementations behind interface (Qualifiers work)
//7 Setter injection... with setters - @Autowired is telling Spring to actually use Setter

@Component
public class ControlCenter {


    //1
    @Autowired
    private DependencyOne dependencyOne;  //very simple - whatever name can be used (matching by type)

    //@Autowired
    //DependencyContract dependencyTwo ;

    //3
    //@Autowired
    //DependencyContract dependencyContract ;

    //4                 -- but we can use @Primary or @Qualifier to solve problem with //3
    //@Autowired
    //DependencyContract dependencyContract ;
    private SomeOtherDependency someOtherDependency;

    //5
    //This does not need @Autowire annotation
/*
    ControlCenter(SomeOtherDependency someOtherDependency){
        this.someOtherDependency = someOtherDependency;
    }
*/

    //6
    // injecting interfaces - works as with @Autowired
/*
    DependencyContract dependencyContract;
    ControlCenter(DependencyContract dependencyContract){
        this.dependencyContract = dependencyContract;
    }
*/


    //7  - with setters - @Autowired is telling Spring to actually use Setter


    private DependencyContract dependencyContract;

    @Autowired  // if you move annotation to above field - no message will be logged - setter not called
    public void setDependencyContract(DependencyContract dependencyContract) {
        System.out.println(" ==== setting dependency!");
        this.dependencyContract = dependencyContract;
    }

    public void control() {
        System.out.println("controlling from : " + ControlCenter.class + "\n");

        //dependencyOne.sayHello();
        //dependencyTwo.sayHello();
        dependencyContract.sayHello();

        //someOtherDependency.hello();
    }
}
