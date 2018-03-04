package com.example;

import com.example.service.CalculationService;

import com.example.tools.Converter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


//1 - show that Main application class is also @Configuration - meaning it Defines beans (and these don't need to be
//annotated with @Service, @Component, etc). But is this much better? Not necessarily, we loose intent but gain other
//things

//2 - introduce another @Configuration class in package configurations
//// - 1 add Converter - don't annotate
//// - 2 add CalculationService - don't annotate
//// - 3 in package confuguration create @Configuration class and defined beans there
//// - 4 show beans can be retrieved from context

//3 - add another SpringDefinedBean to show it is possible to
// mix JAVA configuration (@Configuration) and SPRING configuration (@Component).
// SpringDefinedBean (@Component) uses @Autowired Converter which is defined in @Configurtion class

//4 explain that SpringBootApplication is also @configuration and it could be used to DEFINE beans using @Bean
// - add AdHocBean and retrieve from context




@SpringBootApplication
public class ConfigurationDefinedBeansApp {

    //4
    @Bean
    public AdHocBean adHocBean(){
        return new AdHocBean();
    }

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ConfigurationDefinedBeansApp.class, args);
		String result = ctx.getBean(SpringDefinedBean.class).hello();
        System.out.println(result);

        //retrieve one @Configuration bean
        ctx.getBean(CalculationService.class).service();

        //retrieve another
        System.out.println(ctx.getBean(Converter.class).convert("this is message to convert"));


        System.out.println(ctx.getBean(SpringDefinedBean.class).hello());

        ctx.getBean(AdHocBean.class).hello();


	}
}
