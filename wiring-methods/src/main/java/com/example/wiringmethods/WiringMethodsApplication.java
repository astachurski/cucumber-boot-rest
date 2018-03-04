package com.example.wiringmethods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//1 - demonstrate/ask question - spring can't inject to UNMANAGED classes -
//unmanaged - because WE create NEW instance of WiringMethodsApplication

//2 - if we want Spring to provide dependencies - our destination classes also
//should be managed - we illustrate also using symbolic names for components


@SpringBootApplication
@Component("myapp")

public class WiringMethodsApplication {

	@Autowired
	private ControlCenter controlCenter;

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(WiringMethodsApplication.class, args);

		// 1
		// PROBLEM to think of - why this will throw null pointer exception ?!
        // Spring doesn't work?

		//WiringMethodsApplication app = new WiringMethodsApplication();
		//app.controlCenter.control();


        // 2
        //
        WiringMethodsApplication app = (WiringMethodsApplication)ctx.getBean("myapp");
        app.controlCenter.control();
	}
}
