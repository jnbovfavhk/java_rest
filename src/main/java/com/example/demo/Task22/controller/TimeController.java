package com.example.demo.Task22.controller;

import com.example.demo.Task22.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cities/time")
public class TimeController {
    private final CityService cityService;

    @GetMapping("/{name}")
    public Map<String, String> getTimeByCity(@PathVariable String name) {return cityService.getTimeByCity(name);}
}
