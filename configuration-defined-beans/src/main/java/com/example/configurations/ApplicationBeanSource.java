package com.example.configurations;


import com.example.service.CalculationService;
import com.example.tools.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//how this bean is found? There a @Configuration bean one level above... (Main class)

@Configuration
public class ApplicationBeanSource {

    @Bean
    CalculationService calculationService(){
        return new CalculationService();
    }

    @Bean
    Converter converter(){
        return new Converter();
    }

}
