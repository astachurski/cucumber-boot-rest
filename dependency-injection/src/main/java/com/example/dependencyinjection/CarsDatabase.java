package com.example.dependencyinjection;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarsDatabase {
    public List<Car> findAllCars() {
        List<Car> result = new ArrayList<>();
        result.add(new Car("mercedes", 23));
        result.add(new Car("opel", 12));
        return result;
    }
}