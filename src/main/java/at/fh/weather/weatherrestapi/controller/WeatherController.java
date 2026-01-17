package at.fh.weather.weatherrestapi.controller;

import at.fh.weather.weatherrestapi.service.WeatherApiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WeatherController {

    private final WeatherApiClient weatherApiClient;

    public WeatherController(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }

    @GetMapping("/weather")
    public Map<String, Object> getWeather(@RequestParam String city) {
        return weatherApiClient.getCurrentWeather(city);
    }
}
