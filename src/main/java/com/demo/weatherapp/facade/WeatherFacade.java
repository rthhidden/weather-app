package com.demo.weatherapp.facade;

import com.demo.weatherapp.config.WeatherAppConfig;
import com.demo.weatherapp.infrastructure.WeatherService;
import com.demo.weatherapp.infrastructure.WeatherVO;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class WeatherFacade {

    private final WeatherService weatherService;
    private final WeatherAppConfig config;
    private final Clock clock;
    private final WeatherDTOAssembler weatherDtoAssembler;

    public WeatherFacade(WeatherService weatherService, WeatherAppConfig config, Clock clock) {
        this.weatherService = weatherService;
        this.config = config;
        this.clock = clock;
        this.weatherDtoAssembler = new WeatherDTOAssembler();
    }

    public WeatherDTO getWeather(String city) {
        WeatherVO weatherVO = weatherService.getWeather(city);

        return weatherDtoAssembler.toDto(weatherVO, config, clock);
    }
}
