package com.weather.service;

import com.weather.domain.entity.City;
import com.weather.domain.entity.WeatherData;
import com.weather.repository.CityRepository;
import com.weather.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import com.weather.config.

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;
    private WeatherDataRepository weatherDataRepository;
    @Value("{weather-api-key}")
    private String weather_api_key;
    private final  String apiUrl = "http://api.openweathermap.org/data/2.5/weather";

    public WeatherService(RestTemplate restTemplate, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }
    public WeatherData getWeather(String cityName) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, cityName, weather_api_key);
        var response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response != null) {
            City city = new City();
            city.setName(cityName);
            city.setCountry(response.getSys().getCountry());
            city.setLatitude(response.getCoord().getLat());
            city.setLongitude(response.getCoord().getLon());
            cityRepository.save(city);

            WeatherData weatherData = new WeatherData();
            weatherData.setCity(city);
            weatherData.setTemperature(response.getMain().getTemp());
            weatherData.setWeatherState(response.getWeather().get(0).getDescription());
            weatherData.setHumidity(response.getMain().getHumidity());
            weatherData.setWindSpeed(response.getWind().getSpeed());
            weatherData.setTimestamp(LocalDateTime.now());
            weatherDataRepository.save(weatherData);

            return weatherData;
        }
        return null;
    }
}

