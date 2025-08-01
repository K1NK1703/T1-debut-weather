# T1-Дебют. Weather App ☁️☀️🌧️

## Описание

Этот проект моделирует метеорологическую систему с использованием **Apache Kafka**. Он состоит из двух основных компонентов:

- `WeatherProducer` — генератор и отправитель случайных погодных данных в Kafka-топик.
- `WeatherConsumer` — получатель и аналитик погодных данных, подписанный на этот топик.

---

## Стек технологий

- Java 23
- Apache Kafka
- Kafka Client API
- Maven
- Spring Boot

---

## Установка и запуск

1. Клонировать репозиторий
2. В терминале выполнить команду docker-compose up -d
3. Kafka-UI доступна по адресу http://localhost:8080

---

## Пример сообщения в топике

{
  "dateTime": "14-07-2025 16:20:00",
  "city": "Туапсе",
  "temperature": 21.0,
  "condition": "солнечно"
}
