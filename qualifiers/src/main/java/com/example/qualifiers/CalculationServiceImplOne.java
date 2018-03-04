package com.example.qualifiers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("add")
public class CalculationServiceImplOne implements CalculationService {
    @Override
    public int calculate(int a, int b) {
        return a + b;
    }
}
