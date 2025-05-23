package com.example.demo.Task22.controller;

import com.example.demo.Task22.services.CityService;
import com.example.demo.Task22.services.CityServiceBigData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class TimeController {
    private final CityService cityService;
    private final CityServiceBigData cityServiceBigData;

    @GetMapping("/cities/time/{name}")
    public Map<String, String> getTimeByCity(@PathVariable String name) {return cityService.getTimeByCity(name);}

    @GetMapping("/cities_more_data/time/{name}")
    public Map<String, String> getTimeByCityMoreDate(@PathVariable String name) {return cityServiceBigData.getTimeByCity(name);}
}
