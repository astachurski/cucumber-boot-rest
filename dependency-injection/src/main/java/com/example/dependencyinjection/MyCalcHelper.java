package com.example.dependencyinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyCalcHelper {

    @Autowired
    private CalcAdvisor calcAdvisor;

    public int crunchNumbers(int a, int b) {
        if (calcAdvisor.shouldIcalculate(a, b))
            return a + b;
        else
            return 0;
    }
}
