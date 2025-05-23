package com.example.demo.Task22.services;

import com.example.demo.Task22.entities.CityInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityService {

    private final List<CityInfo> cities = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("cities.csv"), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // пропустить заголовок
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    cities.add(new CityInfo(
                            parts[0],
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            parts[4],
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

    public List<CityInfo> getAllCities() {
        return enrichCitiesWithTime(cities);
    }

    public CityInfo getCityByName(String name) {
        for (CityInfo city : cities) {
            if ((city.getCity() != null) && (city.getCity().toLowerCase().equals(name))) {
                return enrichCityWithTime(city);
            }
        }
        return null;
    }

    public List<CityInfo> getCitiesByCountry(String countryName) {

        return cities.stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(countryName)).toList();
    }

    public Map<String, String> getTimeByCity(String name) {
        Map<String, String> outputMap = new HashMap<>();

        for (CityInfo city : cities) {
            if (city.getCity().equalsIgnoreCase(name)) {
                outputMap.put("utcTime", city.getUtcTime());
                outputMap.put("localTime", city.getLocalTime());
                return outputMap;
            }
        }
        return null;
    }

    private List<CityInfo> enrichCitiesWithTime(List<CityInfo> cityList) {
        List<CityInfo> updated = new ArrayList<>();
        for (CityInfo city : cityList) {
            updated.add(enrichCityWithTime(city));
        }
        return updated;
    }

    private CityInfo enrichCityWithTime(CityInfo city) {
        try {
            // Ищем каждый временной параметр
            // Местное время
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(city.getTimezone()));
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