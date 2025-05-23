package com.example.demo.Task22.controller;

import com.example.demo.Task22.entities.CityInfo;
import com.example.demo.Task22.entities.CityInfoForBiggerData;
import com.example.demo.Task22.services.CityServiceBigData;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cities_more_data")
public class CityBigDataController {
    private final CityServiceBigData cityService;

    @GetMapping
    public List<CityInfoForBiggerData> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/city/{name}")
    public CityInfoForBiggerData getCityByName(@PathVariable String name) {
        return cityService.getCityByName(name.toLowerCase());
    }

    @GetMapping("/country/{country}")
    public List<CityInfoForBiggerData> getCitiesByCountry(@PathVariable String country) {return cityService.getCitiesByCountry(country);}
}
