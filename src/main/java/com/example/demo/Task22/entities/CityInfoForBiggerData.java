package com.example.demo.Task22.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityInfoForBiggerData {
    private String city;
    private String cityAscii;
    private double latitude;
    private double longitude;
    private String country;
    private String iso2;
    private String iso3;
    private String adminName;
    private String capital;
    private Long population;
    private Long id;
    private String localTime;  // текущее местное время
    private String utcTime;    // текущее UTC время в формате UFC (ISO 8601)
    private String timeDescription;


    public String getCity() {
        return cityAscii;
    }
}
