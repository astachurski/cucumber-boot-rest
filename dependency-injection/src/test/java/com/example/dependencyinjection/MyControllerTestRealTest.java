package com.example.dependencyinjection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyControllerTestRealTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MyCalc myCalc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testDependencyLoads() {
        assertNotNull(myCalc);
        assertNotNull(testRestTemplate);
    }

    @Test
    public void controllerShouldReturnCorrectValue() {
        String host = "http://localhost";
        String url = String.format("%s:%s/%s", host, port, "calc/2/3");
        String forObject = testRestTemplate.getForObject(url, String.class);
        assertNotNull(forObject);
        assertEquals("5", forObject);
        System.out.println(forObject.toString());
    }

    @Test
    public void hello() {
        int result = myCalc.add(2, 5);
        System.out.println(result);
    }

}