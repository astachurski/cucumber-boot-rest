package com.example.qualifiers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class QualifiersApplication {

	@Autowired
	//@Qualifier("add")
	@Qualifier("slow")
	private CalculationService calculationService;

	public void hello(){
		System.out.println("hello!");
		System.out.println(calculationService.calculate(3,4));
	}

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(QualifiersApplication.class, args);
        QualifiersApplication app =  ctx.getBean(QualifiersApplication.class);
		app.hello();
	}


}
