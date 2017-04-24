package com.demo.weatherapp.web;

import com.demo.weatherapp.config.WeatherAppConfig;
import com.demo.weatherapp.exception.FetchWeatherException;
import com.demo.weatherapp.facade.WeatherDTO;
import com.demo.weatherapp.facade.WeatherFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private WeatherFacade weatherFacade;
    private WeatherAppConfig config;

    public WeatherController(WeatherFacade weatherFacade, WeatherAppConfig config) {
        this.weatherFacade = weatherFacade;
        this.config = config;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("locations", config.getLocations());

        return modelAndView;
    }

    @PostMapping("/weather")
    public ModelAndView weather(@RequestParam String cityName) {
        WeatherDTO weatherDTO = this.weatherFacade.getWeather(cityName);

        ModelAndView modelAndView = new ModelAndView("weather-details");
        modelAndView.addObject("weather", weatherDTO);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(value = FetchWeatherException.class)
    public ModelAndView fetchWeatherException(FetchWeatherException exception) {
        logger.error("Exception during fetching the weather", exception);

        ModelAndView modelAndView = new ModelAndView("weather-error");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("reason", exception.getMessage());

        return modelAndView;
    }
}
