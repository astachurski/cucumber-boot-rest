package com.example.dependencyinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class MyController {

    @Autowired
    private CarsDatabase carsDatabase;

/*
    @Autowired
    private MyCalc myCalc; <----------- 1 way (field injection)
*/

    private MyCalc myCalc;

   /* @Autowired
    public void setMyCalc(MyCalc myCalc) { < -------- 2 way (setter injection)
        this.myCalc = myCalc;
    }*/

    public MyController(MyCalc myCalc) {  // < --------- 3 way (constructor injection)
        this.myCalc = myCalc;
    }


    @RequestMapping("hello/{a}/{b}") //passing parameters in REST style
    @ResponseBody
    public String hello(@PathVariable(name = "a") int a,
                        @PathVariable(name = "b") int b) {
        return String.valueOf(myCalc.add(a, b));

    }

    @RequestMapping("calc/{a}/{b}")
    @ResponseBody
    public String calculate(@PathVariable(name = "a") int a,
                            @PathVariable(name = "b") int b) {
        return String.valueOf(myCalc.add(a, b));
    }

    @RequestMapping(value = "/savecar", method = RequestMethod.POST)
    @ResponseBody
    public Car getResponse(@RequestBody Car car) {
        System.out.println("saving to database: " + car.toString());
        //use dependency to save it!
        return car;
    }

    @RequestMapping("/response1")
    public Car getResponse() {
        Car car = new Car("skoda", 23);
        return car;
    }

    //example of inner class. DTO is for data transfer object
    private class CarsDTO {
        private List<Car> cars;

        public void setCars(List<Car> cars) {
            this.cars = cars;
        }

        public List<Car> getCars() {
            return cars;
        }
    }

    @RequestMapping(value = "/getcars") //response will be json object with list of objects
    public CarsDTO getCars() {
        CarsDTO carsDTO = new CarsDTO();
        carsDTO.setCars(carsDatabase.findAllCars());
        return carsDTO;
    }

}
