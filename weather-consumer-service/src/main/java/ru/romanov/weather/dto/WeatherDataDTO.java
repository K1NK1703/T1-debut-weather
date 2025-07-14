package ru.romanov.weather.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record WeatherDataDTO(
        @JsonProperty("dateTime") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dateTime,
        @JsonProperty("city") String city,
        @JsonProperty("temperature") double temperature,
        @JsonProperty("condition") String condition
) {}
