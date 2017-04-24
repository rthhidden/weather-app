package com.demo.weatherapp.acceptance.steps;

import com.demo.weatherapp.acceptance.mock.WeatherApiMock;
import cucumber.api.java.en.Given;

public class WeatherMockSteps extends AbstractSteps {

    private final WeatherApiMock weatherApiMock;

    public WeatherMockSteps(WeatherApiMock weatherApiMock) {
        this.weatherApiMock = weatherApiMock;
    }

    @Given("^weather api returns following data for \"([^\"]*)\":$")
    public void weatherApiReturnsFollowingDataForCity(String city, String json) throws Throwable {
        weatherApiMock.prime(city, json);
    }

    @Given("^weather api is down$")
    public void weatherApiIsDown() throws Throwable {
        weatherApiMock.stop();
    }

    @Given("^weather api is not responding$")
    public void weatherApiIsNotResponding() throws Throwable {
        weatherApiMock.primeReadTimeout();
    }

    @Given("^weather api for \"([^\"]*)\" returns status code \"([^\"]*)\" and json:$")
    public void weatherApiReturnsStatusCodeAndJson(String city, int non200, String json) throws Throwable {
        weatherApiMock.primeNon200(city, non200, json);
    }
}
