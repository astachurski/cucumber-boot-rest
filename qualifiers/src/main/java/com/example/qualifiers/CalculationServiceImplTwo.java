package com.example.qualifiers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@Primary
@Qualifier("multiply")
public class CalculationServiceImplTwo implements CalculationService {
    @Override
    public int calculate(int a, int b) {
        return a * b;
    }
}
