package com.weather.controller;

import com.weather.service.WeatherService;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String Home(){
        return "home";
    }

    @GetMapping("/weather")
    public String Weather(){
        return "weather";
    }
}
