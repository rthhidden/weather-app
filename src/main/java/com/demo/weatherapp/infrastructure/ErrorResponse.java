package com.demo.weatherapp.infrastructure;

public interface ErrorResponse {

    String CONNECT_TIMEOUT = "Failure talking to weather server - Could not connect";

    String READ_TIMEOUT = "Failure talking to weather server - Read timeout";

    String INVALID_CONTENT = "Failure fetching weather content. Weather server content invalid.";

    String STATUS_CODE_ERROR = "Failure fetching weather content. Weather server responded with status %s";

}
