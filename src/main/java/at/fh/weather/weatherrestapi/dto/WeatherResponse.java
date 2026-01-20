package at.fh.weather.weatherrestapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponse {

    private String city;
    private String country;
    private double temperature;
    private String condition;
    private int humidity;
    private double windKmh;
    private String lastUpdated;

    public WeatherResponse() {
    }

    public WeatherResponse(String city, String country, double temperature, String condition, int humidity, double windKmh, String lastUpdated) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windKmh = windKmh;
        this.lastUpdated = lastUpdated;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("temperature_celsius")
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @JsonProperty("wind_kmh")
    public double getWindKmh() {
        return windKmh;
    }

    public void setWindKmh(double windKmh) {
        this.windKmh = windKmh;
    }

    @JsonProperty("last_updated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
