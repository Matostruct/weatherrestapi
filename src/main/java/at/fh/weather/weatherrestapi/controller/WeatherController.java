package at.fh.weather.weatherrestapi.controller;

import at.fh.weather.weatherrestapi.dto.WeatherResponse;
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

    @GetMapping("/")
    public String index() {
        return "index";
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
            WeatherResponse response = parseWeatherResponse(weather);
            return ResponseEntity.ok(response);
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

    private WeatherResponse parseWeatherResponse(Map<String, Object> apiResponse) {
        Map<String, Object> location = (Map<String, Object>) apiResponse.get("location");
        Map<String, Object> current = (Map<String, Object>) apiResponse.get("current");
        Map<String, Object> condition = (Map<String, Object>) current.get("condition");

        return new WeatherResponse(
                (String) location.get("name"),
                (String) location.get("country"),
                ((Number) current.get("temp_c")).doubleValue(),
                (String) condition.get("text"),
                ((Number) current.get("humidity")).intValue(),
                ((Number) current.get("wind_kph")).doubleValue(),
                (String) current.get("last_updated")
        );
    }
}
