package com.example.configurationhandling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


//1
// in Bean to configure demonstrate @Value annotation - the data are sucked in from
// application.properties

//2
// demonstrate profiles in Spring - show that we can "personalize" application configuration
// add dev and adrian properties
// we can use multiple profiles at once - e.g "adrian,dev" - we can split our configurations
// this way

//3
// show how you can access environment - through spring context

//4
// we can also use YAML based format - you can show how to access list elements

//5 add yet another class

@SpringBootApplication
public class ConfigurationHandlingApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ConfigurationHandlingApplication.class, args);

        (ctx.getBean(BeanToConfigure.class)).hello();

        System.out.println(" -------- profiles in use ---------");

        for (String s : ctx.getEnvironment().getActiveProfiles()) {
            System.out.println(s);
        }


        //3
        System.out.println("PROP name in env: " + ctx.getEnvironment().getProperty("name"));
        System.out.println("PROP age in env: " + ctx.getEnvironment().getProperty("age"));

        //4
        System.out.println("dev.url: " + ctx.getEnvironment().getProperty("environments.dev.url"));
        System.out.println("prod.url: " + ctx.getEnvironment().getProperty("environments.prod.url"));

        //5
        (ctx.getBean(BeanToConfigureSecond.class)).hello();

    }
}
