package com.example.demo.Task23;

import com.example.demo.Task22.entities.CityInfoForBiggerData;
import com.example.demo.Task22.services.CityServiceBigData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CityViewController {
    private final CityServiceBigData cityService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "index";
    }


    @GetMapping("/search")
    public String search(@RequestParam(required = false) String city,
                         @RequestParam(required = false) String country,
                         @RequestParam(required = false) Integer timeZone,
                         Model model) {
        List<CityInfoForBiggerData> results = cityService.getCitiesByFilters(city, country, timeZone);
        model.addAttribute("cities", results);
        return "index";
    }


    @GetMapping("/city/{id}")
    public String cityDetails(@PathVariable String name, Model model) throws Exception {
        CityInfoForBiggerData city = cityService.getCityByName(name);
        if (city == null) throw new Exception();
        model.addAttribute("city", city);
        return "city";
    }


    @ExceptionHandler(Exception.class)
    public String handleError() {
        return "error";
    }
}
