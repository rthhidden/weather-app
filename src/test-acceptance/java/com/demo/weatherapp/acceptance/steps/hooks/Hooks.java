package com.demo.weatherapp.acceptance.steps.hooks;

import com.demo.weatherapp.acceptance.mock.WeatherApiMock;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    private final WeatherApiMock weatherApiMock;

    public Hooks(WeatherApiMock weatherApiMock) {
        this.weatherApiMock = weatherApiMock;
    }

    @Before
    public void beforeScenario() {
        weatherApiMock.start();
    }

    @After
    public void afterScenario() {
        weatherApiMock.reset();
    }
}
