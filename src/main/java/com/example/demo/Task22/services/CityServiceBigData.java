package com.example.demo.Task22.services;

import com.example.demo.Task22.entities.CityInfo;
import com.example.demo.Task22.entities.CityInfoForBiggerData;
import jakarta.annotation.PostConstruct;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CityServiceBigData {

    private final List<CityInfoForBiggerData> cities = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("worldcities.csv"), StandardCharsets.UTF_8))) {

            String line;
            reader.readLine(); // пропустить заголовок

            ConversionService conversionService = DefaultConversionService.getSharedInstance();

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].startsWith("\"") && parts[i].endsWith("\"")) {
                        parts[i] = parts[i].substring(1, parts[i].length() - 1);
                    }
                }

                if (parts.length == 11) {
                    Long population;
                    Long id;
                    try {
                        population = conversionService.convert(parts[9], Long.class);
                        id = conversionService.convert(parts[10], Long.class);
                    } catch (ConversionFailedException e) {
                        population = 0L;
                        id = 0L;
                    }

                    cities.add(new CityInfoForBiggerData(
                            parts[0],
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            parts[4],
                            parts[5],
                            parts[6],
                            parts[7],
                            parts[8],
                            population,
                            id,
                            null, // временно, заполним позже
                            null,
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CityInfoForBiggerData> getAllCities() {
        return enrichCitiesWithTime(cities);
    }

    public CityInfoForBiggerData getCityByName(String name) {
        for (CityInfoForBiggerData city : cities) {
            if ((city.getCity() != null) && (city.getCity().toLowerCase().equals(name))) {
                return enrichCityWithTime(city);
            }
        }
        return null;
    }

    public List<CityInfoForBiggerData> getCitiesByCountry(String countryName) {

        return cities.stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(countryName)).toList();
    }

    public Map<String, String> getTimeByCity(String name) {
        Map<String, String> outputMap = new HashMap<>();

        for (CityInfoForBiggerData city : cities) {
            if (city.getCity().equalsIgnoreCase(name)) {
                outputMap.put("utcTime", city.getUtcTime());
                outputMap.put("localTime", city.getLocalTime());
                return outputMap;
            }
        }
        return null;
    }

    private List<CityInfoForBiggerData> enrichCitiesWithTime(List<CityInfoForBiggerData> cityList) {
        List<CityInfoForBiggerData> updated = new ArrayList<>();
        for (CityInfoForBiggerData city : cityList) {
            updated.add(enrichCityWithTime(city));
        }
        return updated;
    }

    private CityInfoForBiggerData enrichCityWithTime(CityInfoForBiggerData city) {
        try {
            // Ищем каждый временной параметр
            // Местное время

            // Получаем все доступные зоны и ищем подходящую
            String possibleZoneId = ZoneId.getAvailableZoneIds().stream()
                    .filter(zone -> zone.contains(city.getCountry()) || zone.contains(city.getCity()))
                    .findFirst()
                    .orElse("UTC"); // Если не нашли, используем UTC


            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(possibleZoneId));
            city.setLocalTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            // UTC время
            city.setUtcTime(Instant.now().toString()); // UFC-формат (ISO 8601, UTC)
            // Смещение локального и UTC времени
            int timeDifference = now.getOffset().getTotalSeconds() / 3600;

            // Делаем строку нужного вида
            StringBuilder timeDesc = new StringBuilder();
            timeDesc.append(city.getCity()).append(": ");
            timeDesc.append(now.format(DateTimeFormatter.ofPattern("HH:mm"))).append(" (");
            timeDesc.append(String.format("%+d UTC)", timeDifference));

            city.setTimeDescription(timeDesc.toString());
        } catch (Exception e) {
            city.setLocalTime("Unknown");
            city.setUtcTime("Unknown");
        }
        return city;
    }


}
