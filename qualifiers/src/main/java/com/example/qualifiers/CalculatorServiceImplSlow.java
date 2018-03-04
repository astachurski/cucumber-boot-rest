package com.example.qualifiers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("slow")
public class CalculatorServiceImplSlow implements CalculationService{
    @Override
    public int calculate(int a, int b) {
        try {
            System.out.println("sleeping...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a + b;
    }
}
