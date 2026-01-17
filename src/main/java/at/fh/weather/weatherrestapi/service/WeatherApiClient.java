package at.fh.weather.weatherrestapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class WeatherApiClient {

    private final String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherApiClient(@Value("${weatherapi.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<String, Object> getCurrentWeather(String city) {
        String url = UriComponentsBuilder
                .fromUriString("http://api.weatherapi.com/v1/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", city)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }
}
