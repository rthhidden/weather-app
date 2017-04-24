package com.demo.weatherapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties("weather")
public class WeatherAppConfig {

    private List<String> locations = Arrays.asList("London", "Hong Kong");
    private String dateFormat = "dd-MM-yyyy";
    private String sunriseSunsetDateFormat = "h:mm a";
    private ApiConfig api = new ApiConfig();

    public List<String> getLocations() {
        return this.locations;
    }

    @SuppressWarnings("unused")
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public ApiConfig getApi() {
        return api;
    }

    public void setApi(ApiConfig api) {
        this.api = api;
    }

    @SuppressWarnings("unused")
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getSunriseSunsetDateFormat() {
        return sunriseSunsetDateFormat;
    }

    @SuppressWarnings("unused")
    public void setSunriseSunsetDateFormat(String sunriseSunsetDateFormat) {
        this.sunriseSunsetDateFormat = sunriseSunsetDateFormat;
    }
}
