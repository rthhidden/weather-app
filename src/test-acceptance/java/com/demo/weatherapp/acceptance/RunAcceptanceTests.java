package com.demo.weatherapp.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test-acceptance/resources/")
public class RunAcceptanceTests {
}
