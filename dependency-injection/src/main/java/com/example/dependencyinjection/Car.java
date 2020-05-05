package com.example.dependencyinjection;

public class Car {
    private String brand;
    private int age;

    public Car(String brand, int age) {
        this.brand = brand;
        this.age = age;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBrand() {
        return brand;
    }

    public int getAge() {
        return age;
    }
}
