package com.example.dependencyinjection;

import org.springframework.stereotype.Component;

@Component
public class CalcAdvisorTwo implements CalcAdvisor {
    @Override
    public boolean shouldIcalculate(int a, int b) {
        return true;
    }
}
