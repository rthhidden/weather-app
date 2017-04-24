package com.demo.weatherapp.facade;

import com.demo.weatherapp.config.WeatherAppConfig;
import com.demo.weatherapp.infrastructure.WeatherVO;
import com.demo.weatherapp.util.TemperatureUtil;
import com.demo.weatherapp.util.Util;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.demo.weatherapp.util.DateUtil.format;
import static com.demo.weatherapp.util.DateUtil.now;

class WeatherDTOAssembler {

    private static final String TEMPERATURE_FORMAT = "%4.2f";
    private static final String NOT_AVAILABLE = "N/A";

    WeatherDTO toDto(WeatherVO weatherVO, WeatherAppConfig weatherAppConfig, Clock clock) {

        String city = weatherVO.getCity().orElse(NOT_AVAILABLE);
        String todayDate = format(weatherAppConfig.getDateFormat(), now(clock));
        String celsius = formatTemperature(weatherVO, TemperatureUtil::getCelsiusFromKelvin);
        String fahrenheit = formatTemperature(weatherVO, TemperatureUtil::getFahrenheitFromKelvin);
        String sunrise = formatDate(weatherVO.getSunrise(), weatherAppConfig);
        String sunset = formatDate(weatherVO.getSunset(), weatherAppConfig);
        String weatherDescription = formatWeatherDescription(weatherVO);

        return new WeatherDTO.WeatherDTOBuilder()
                .withCity(city)
                .withTodayDate(todayDate)
                .withTemperatureCelsius(celsius)
                .withTemperatureFahrenheit(fahrenheit)
                .withWeatherDescription(weatherDescription)
                .withSunrise(sunrise)
                .withSunset(sunset)
                .build();
    }

    private String formatDate(Optional<Instant> instantOptional, WeatherAppConfig weatherAppConfig) {
        return instantOptional
                .map(s -> format(weatherAppConfig.getSunriseSunsetDateFormat(), s))
                .orElse(NOT_AVAILABLE);
    }

    private String formatWeatherDescription(WeatherVO weatherVO) {
        String description = weatherVO.getWeatherDetailsList()
                .stream()
                .map(WeatherVO.WeatherDetailsVO::getDescription)
                .filter(Util::isNotEmpty)
                .collect(Collectors.joining(","));

        return description.isEmpty() ? NOT_AVAILABLE : description;
    }

    private String formatTemperature(WeatherVO weatherVO, Function<BigDecimal, BigDecimal> mapper) {
        return weatherVO.getTemperatureKelvin()
                .map(mapper)
                .map(this::formatTemperature)
                .orElse(NOT_AVAILABLE);
    }

    private String formatTemperature(BigDecimal temperature) {
        return String.format(TEMPERATURE_FORMAT, temperature.doubleValue());
    }
}
