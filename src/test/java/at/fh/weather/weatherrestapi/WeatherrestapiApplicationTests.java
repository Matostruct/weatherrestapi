package at.fh.weather.weatherrestapi;

import at.fh.weather.weatherrestapi.controller.WeatherController;
import at.fh.weather.weatherrestapi.dto.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherrestapiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetWeatherMissingCity() throws Exception {
        mockMvc.perform(get("/weather"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Required parameter 'city' is missing"));
    }

    @Test
    void testGetWeatherWithEmptyCity() throws Exception {
        mockMvc.perform(get("/weather?city="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testGetWeatherResponseStructure() throws Exception {
        // This test verifies the response structure (it will fail without valid API key)
        // In production, you would mock the service
        mockMvc.perform(get("/weather?city=Vienna"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").exists())
                .andExpect(jsonPath("$.country").exists())
                .andExpect(jsonPath("$.temperature_celsius").exists())
                .andExpect(jsonPath("$.condition").exists())
                .andExpect(jsonPath("$.humidity").exists())
                .andExpect(jsonPath("$.wind_kmh").exists())
                .andExpect(jsonPath("$.last_updated").exists());
    }
}
