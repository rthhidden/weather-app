package com.demo.weatherapp.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/cucumber" },
        features = "src/test-acceptance",
        glue = "com.demo.weatherapp.acceptance.steps"
)
public class RunAcceptanceTests {
}
