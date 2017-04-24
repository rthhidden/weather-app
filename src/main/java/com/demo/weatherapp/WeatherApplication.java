package com.demo.weatherapp;

import com.demo.weatherapp.config.ApiConfig;
import com.demo.weatherapp.config.WeatherAppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.function.Supplier;

@SpringBootApplication
@EnableConfigurationProperties(WeatherAppConfig.class)
public class WeatherApplication {

    @Bean
    public ApiConfig apiConfig(WeatherAppConfig weatherAppConfig) {
        return weatherAppConfig.getApi();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }
}
