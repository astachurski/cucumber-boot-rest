package com.example.dependencyinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyCalc {

    @Autowired
    private MyCalcHelper myCalcHelper;

    public int add(int a, int b) {
        //return a+b; < -------- 1
        return myCalcHelper.crunchNumbers(a, b); // <--- after dependency added
    }
}
