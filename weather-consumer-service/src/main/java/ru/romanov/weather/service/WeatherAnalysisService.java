package ru.romanov.weather.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.romanov.weather.dto.WeatherDataDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherAnalysisService {

    Map<String, CityWeatherStats> cityStats = new HashMap<>();

    public void analyzeWeatherData(WeatherDataDTO weatherData) {
        String city = weatherData.city();
        cityStats.putIfAbsent(city, new CityWeatherStats(city));

        CityWeatherStats stats = cityStats.get(city);
        stats.updateStats(weatherData);

        if (stats.getReceivedCount() % 5 == 0) {
            System.out.printf(String.format("%n=== Анализ погоды для %s ===%n", city));
            System.out.println(stats.getSummary());
        }
    }

    private static class CityWeatherStats {
        @Getter
        private final String city;
        private int sunnyDays = 0;
        private int rainyDays = 0;
        private int cloudyDays = 0;
        private int snowyDays = 0;
        private double maxTemp = Double.MIN_VALUE;
        private double minTemp = Double.MAX_VALUE;
        private double tempSum = 0;
        @Getter
        private int receivedCount = 0;
        private LocalDateTime maxTempDate;
        private LocalDateTime minTempDate;

        public CityWeatherStats(String city) {
            this.city = city;
        }

        public void updateStats(WeatherDataDTO weatherData) {
            receivedCount++;
            tempSum += weatherData.temperature();

            if (weatherData.temperature() > maxTemp) {
                maxTemp = weatherData.temperature();
                maxTempDate = weatherData.dateTime();
            }

            if (weatherData.temperature() < minTemp) {
                minTemp = weatherData.temperature();
                minTempDate = weatherData.dateTime();
            }

            switch (weatherData.condition()) {
                case "солнечно" -> sunnyDays++;
                case "дождливо", "гроза" -> rainyDays++;
                case "облачно", "туман" -> cloudyDays++;
                case "снежно" -> snowyDays++;
            }
        }

        public String getSummary() {
            double avgTemp = tempSum / receivedCount;
            return String.format(
                    """
                    Общее количество отчётов: %d
                    Солнечных дней: %d
                    Дождливых дней: %d
                    Облачных дней: %d
                    Снежных дней: %d
                    Максимальная температура: %.1f°C on %s
                    Минимальная температура: %.1f°C on %s
                    Средняя температура: %.1f°C
                    """,
                    receivedCount, sunnyDays, rainyDays, cloudyDays, snowyDays,
                    maxTemp, maxTempDate, minTemp, minTempDate, avgTemp);
        }
    }
}
