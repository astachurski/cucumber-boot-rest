package com.example.componentscan.application;

import com.example.componentscan.beans.MainBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//Write application , run and then explain what is component scan - how @Configuration class
// needs to be able to scan packagages (which?) to find all @Annotated candidates for beans.

//this is because it has to do with Java way of configuring beans. Spring used to use XML file
//to list beans - but java added @Configuration and now beans may be scattered all around so
//we can specify as literal ARRAY (as literal) of packages to scan for beans
//String[] names = new String[] {"Ryan", "Julie", "Bob"};

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.componentscan"})
public class ComponentScanApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ComponentScanApplication.class, args);
		ctx.getBean(MainBean.class).useDependency();
	}
}
