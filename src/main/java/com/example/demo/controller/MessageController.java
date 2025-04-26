package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class MessageController {
    @GetMapping("/sayhello")
    public String sayHello() {
        return "Hello, visitor";
    }

    @PostMapping("/echo")
    public String echo(@RequestBody Message input) {

        return input.getText();
    }
}
