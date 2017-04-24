package com.demo.weatherapp.acceptance.steps;

import com.demo.weatherapp.acceptance.helper.RequestHelper;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class WeatherAppSteps extends AbstractSteps {

    private final RequestHelper requestHelper;

    public WeatherAppSteps(RequestHelper requestHelper) {
        this.requestHelper = requestHelper;
    }

    @Given("^want to fetch weather for \"([^\"]*)\"$")
    public void wantToFetchWeatherFor(String cityName) throws Throwable {
        requestHelper.postWeatherData(cityName);
    }

    @Then("^status code is \"([^\"]*)\"$")
    public void statusCodeIs(int expectedStatusCode) throws Throwable {
        assertThat(requestHelper.getStatusCode()).isEqualTo(expectedStatusCode);

    }

    @And("^following details are returned$")
    public void followingDetailsAreReturned(List<String> rows) throws Throwable {
        String body = requestHelper.getBody();
        rows.forEach(row -> assertThat(body)
                .withFailMessage(format("%s not found in %s", row, body))
                .contains(row.trim())
        );
    }

    @When("^I request home page$")
    public void iRequestHomePage() throws Throwable {
        requestHelper.requestHomePage();
    }

    @When("^I request page \"([^\"]*)\"$")
    public void iRequestPage(String url) throws Throwable {
        requestHelper.request(url);
    }
}
