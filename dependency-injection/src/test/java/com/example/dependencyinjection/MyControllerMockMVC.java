package com.example.dependencyinjection;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MyControllerMockMVC {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarsDatabase carsDatabaseMock;

    @Test
    public void controllerShouldReturnCorrectCar() throws Exception {
        String expectedJson = "{\"brand\":\"skoda\",\"age\":23}";
        this.mockMvc
                .perform(get("/response1"))
                .andDo(print())
                .andExpect(content().string(containsString(expectedJson)));
    }

    @Test
    public void controllerShouldReturnSkodaCar() throws Exception {
        this.mockMvc
                .perform(get("/response1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is("skoda")))
                .andExpect(jsonPath("$.age", is(23)));
    }

    //for below test to pass - we need to comment @MockBean because for other
    //test case - we use mocked CarsDatabase.
    //Typicall - this is not necessary because we consistently either USE mocked
    //depenency or NOT.

    @Test
    public void controllerShouldReturn2Cars() throws Exception {
        this.mockMvc
                .perform(get("/getcars"))
                .andDo(print())
                .andExpect(jsonPath("$.cars.length()", is(2)))
                .andExpect(status().isOk());
    }

    @Test
    public void controllerShouldSaveCar() throws Exception {
        String testJson = "{\"brand\":\"skoda\",\"age\":23}";
        Car car = new Car("bmw", 23);

        this.mockMvc
                .perform(post("/savecar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJson))
                .andDo(print());
    }

    @Test
    public void controllerShouldReturnCorrectValue() throws Exception {
        this.mockMvc
                .perform(get("/calc/3/4"))
                .andDo(print())// --------------------------- useful in debug and writing tests
                .andExpect(status().isOk())  // ----------mapping of HTTP statuses
                .andExpect(content().string(containsString("7")));
    }

    @Test
    public void shouldSecondCarBeToyotaWithMockedDependency() throws Exception {
        List<Car> testCars = new ArrayList<>();
        testCars.add(new Car("honda", 11));
        testCars.add(new Car("toyota", 2));

        when(carsDatabaseMock.findAllCars()).thenReturn(testCars);

        this.mockMvc
                .perform(get("/getcars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cars[1].brand", is("toyota")))
                .andExpect(jsonPath("$.cars[1].age", is(2)));

    }


}