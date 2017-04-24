package com.demo.weatherapp.acceptance.steps;

import com.demo.weatherapp.WeatherApplication;
import com.demo.weatherapp.acceptance.FixedClockProvider;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {WeatherApplication.class, FixedClockProvider.class})
@TestPropertySource(locations = "classpath:test.properties")
//This is deprecated but unfortunately could not get the server to start without it
@SuppressWarnings("deprecation")
@org.springframework.boot.test.IntegrationTest
public class AbstractSteps {
}
