package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${name}")
    private String name;

    @Value("${hello.first-name:}")
    private String firstName;

    @Value("${hello.last-name:}")
    private String lastName;

    @Value("${hello.env:qa}")
    private String env;

    @GetMapping("/hello")
    public String handle() {
        logger.info("Hey "+name+", env="+System.getenv("SPRING_PROFILE"));
        logger.info("Hello "+name+": firstName="+firstName+", lastName="+lastName+", env"+env);

        return "Hello "+name+": firstName="+firstName+", lastName="+lastName+", env"+env;
    }
}
