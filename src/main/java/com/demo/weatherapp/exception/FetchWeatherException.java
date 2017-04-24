package com.demo.weatherapp.exception;

public class FetchWeatherException extends RuntimeException {

    public FetchWeatherException(String s, Throwable cause) {
        super(s, cause);
    }
}
