package ru.romanov.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.romanov.weather.dto.WeatherDataDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherConsumerService {

    WeatherAnalysisService weatherAnalysisService;
    ObjectMapper objectMapper;

    public WeatherConsumerService(WeatherAnalysisService weatherAnalysisService,
                                  ObjectMapper objectMapper) {
        this.weatherAnalysisService = weatherAnalysisService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "weather-topic", groupId = "weather-consumer-group")
    public void consumeWeatherData(String weatherJson) throws JsonProcessingException {
        WeatherDataDTO weatherData = objectMapper.readValue(weatherJson, WeatherDataDTO.class);
        System.out.printf(String.format("Consumed weather data: %s%n", weatherJson));
        weatherAnalysisService.analyzeWeatherData(weatherData);
    }
}
