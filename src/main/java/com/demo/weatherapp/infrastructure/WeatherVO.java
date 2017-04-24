package com.demo.weatherapp.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.demo.weatherapp.util.DateUtil.secondsToInstant;

public class WeatherVO {

    private static final Logger logger = LoggerFactory.getLogger(WeatherVO.class);

    private final String city;
    private final Instant sunrise;
    private final Instant sunset;
    private final BigDecimal temperatureKelvin;
    private final List<WeatherDetailsVO> weatherApiDetailsVOList;

    @JsonCreator
    public WeatherVO(
            @JsonProperty("name") String city,
            @JsonProperty("main") Map<String, Object> main,
            @JsonProperty("sys") Map<String, Object> sys,
            @JsonProperty("weather") List<WeatherDetailsVO> weatherApiDetailsVOList) {

        this.city = city;
        this.weatherApiDetailsVOList = getWeatherDetails(weatherApiDetailsVOList);
        this.sunrise = getInstant(attribute(sys, "sunrise"));
        this.sunset = getInstant(attribute(sys, "sunset"));
        this.temperatureKelvin = getBigDecimal(attribute(main, "temp"));
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public Optional<Instant> getSunrise() {
        return Optional.ofNullable(sunrise);
    }

    public Optional<Instant> getSunset() {
        return Optional.ofNullable(sunset);
    }

    public Optional<BigDecimal> getTemperatureKelvin() {
        return Optional.ofNullable(temperatureKelvin);
    }

    public List<WeatherDetailsVO> getWeatherDetailsList() {
        return Collections.unmodifiableList(weatherApiDetailsVOList);
    }

    private Instant getInstant(Object epochSecond) {
        if (epochSecond == null) {
            return null;
        }
        try {
            return secondsToInstant(String.valueOf(epochSecond));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("seconds invalid: " + epochSecond);
        }
    }

    private BigDecimal getBigDecimal(Object temperature) {
        if (temperature == null) {
            return null;
        }
        try {
            return new BigDecimal(String.valueOf(temperature));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("temperature invalid: " + temperature);
        }
    }

    private List<WeatherDetailsVO> getWeatherDetails(List<WeatherDetailsVO> weatherApiDetailsVOList) {
        return weatherApiDetailsVOList != null ? weatherApiDetailsVOList : Collections.emptyList();
    }

    private Object attribute(Map<String, Object> values, String key) {
        if (values == null) {
            logger.warn("values are missing for key {}", key);
            return null;
        }

        if (!values.containsKey(key)) {
            logger.warn("key {} is missing from weather content {}", key, values);
        }

        return values.get(key);
    }

    public static class WeatherDetailsVO {
        private String description;

        public String getDescription() {
            return description;
        }
    }
}
