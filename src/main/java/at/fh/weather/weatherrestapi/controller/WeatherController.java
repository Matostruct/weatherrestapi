package at.fh.weather.weatherrestapi.controller;

import at.fh.weather.weatherrestapi.service.WeatherApiClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WeatherController {

    private final WeatherApiClient weatherApiClient;

    public WeatherController(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam(required = false) String city) {
        if (city == null || city.isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "Required parameter 'city' is missing");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            Map<String, Object> weather = weatherApiClient.getCurrentWeather(city);
            return ResponseEntity.ok(weather);
        } catch (HttpClientErrorException e) {
            Map<String, Object> error = new HashMap<>();
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                error.put("status", 403);
                error.put("error", "Forbidden");
                error.put("message", "API Key missing or invalid");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                error.put("status", 404);
                error.put("error", "Not Found");
                error.put("message", "City not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else {
                error.put("status", 502);
                error.put("error", "Bad Gateway");
                error.put("message", "Weather API unreachable or returned an error");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("error", "Internal Server Error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
