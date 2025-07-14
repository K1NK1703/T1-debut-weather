package ru.romanov.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.romanov.weather.dto.WeatherDataDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherProducerService {

    static String TOPIC_NAME = "weather-topic";

    static List<String> CITIES = List.of(
            "Москва", "Санкт-Петербург", "Туапсе", "Краснодар",
            "Тюмень", "Сочи", "Казань", "Владивосток", "Новосибирск"
    );

    static List<String> CONDITIONS  = List.of(
            "солнечно", "облачно", "дождливо", "гроза", "туман", "снежно"
    );

    Random random = new Random();
    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper;

    public WeatherProducerService(KafkaTemplate<String, String> kafkaTemplate,
                                  ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 2000)
    public void produceWeatherData() throws JsonProcessingException {
        String city = CITIES.get(random.nextInt(CITIES.size()));
        LocalDateTime date = LocalDateTime.now().minusDays(random.nextInt(7));
        double temperature = Math.round((1 + (35 * random.nextDouble()) * 10) / 10.0);
        String condition = CONDITIONS.get(random.nextInt(CONDITIONS.size()));
        WeatherDataDTO weatherData = new WeatherDataDTO(date, city, temperature, condition);
        String weatherJson = objectMapper.writeValueAsString(weatherData);

        kafkaTemplate.send(TOPIC_NAME, city, weatherJson);
        System.out.printf("Produced weather data: %s%n", weatherJson);
    }
}
