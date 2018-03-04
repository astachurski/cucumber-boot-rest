package com.example.tools;

//converter is NOT annotated - the bean definition is in @Configuration class (main application)

public class Converter {
    public String convert(String arg){
        return arg.toUpperCase();
    }
}
