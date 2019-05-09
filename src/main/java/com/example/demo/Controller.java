package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Value("${name}")
    private String name;

    @GetMapping("/hello")
    public String handle() {
        return "Hello "+name;
    }
}
