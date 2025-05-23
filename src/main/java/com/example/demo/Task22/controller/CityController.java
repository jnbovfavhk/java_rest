package com.example.demo.Task22.controller;



import com.example.demo.Task22.entities.CityInfo;
import com.example.demo.Task22.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public List<CityInfo> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/city/{name}")
    public CityInfo getCityByName(@PathVariable String name) {
        return cityService.getCityByName(name.toLowerCase());
    }

    @GetMapping("/country/{country}")
    public List<CityInfo> getCitiesByCountry(@PathVariable String country) {return cityService.getCitiesByCountry(country);}
}
