package com.example.qualifiers;

import org.springframework.stereotype.Service;

@Service
public interface CalculationService {
    int calculate(int a, int b);
}
