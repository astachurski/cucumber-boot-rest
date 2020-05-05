package com.example.dependencyinjection;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class CalcAdvisorOne implements CalcAdvisor {
    @Override
    public boolean shouldIcalculate(int a, int b) {
        return true;
        //return a % b == 0;
    }
}
