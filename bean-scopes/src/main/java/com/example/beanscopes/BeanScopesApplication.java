package com.example.beanscopes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

//1 - retrieve 3 times Bean which is configured with default scope which is... singleton
//2 - we can get new, fresh instance every time with @Scope controlled annotation
//3 - nest dependencies and observe behaviour - dependency with default SCOPE - all works fine
//4 - we want to have prototypes in NESTED dependencies - works fine with BOTH prototypes and BOTH singletons
//    it works finen if MainClass is prototype and dependency is singleton
//5 - we want dependency to be prototype but MainClass to be singleton - IT DOESN'T WORK - dependency is always the same

@SpringBootApplication
public class BeanScopesApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BeanScopesApplication.class, args);

		//1 - show toString shows the same 3 times

/*
		MainClass mainClass = ctx.getBean(MainClass.class);
		mainClass.hello();
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();
*/


        //2 - now uncomment @Scope in MainClass and observe new, fresh instances
/*        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();*/

        //3 - now - let's nest dependencies - add Dependency instance do MainClass - with default dependency scope
        // behaviour - dependency is not changing - it is OKAY but in //4...
/*
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();
*/

        //4 - prototypes in dependencies work just fine. MainClass and dependencies are different every time
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();
        ctx.getBean(MainClass.class).hello();


	}
}
