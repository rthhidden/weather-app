package com.demo.weatherapp.acceptance;

import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class FixedClockProvider {

    @Bean
    public Clock clock() {
        return  Clock.fixed(Instant.parse("2017-02-09T11:20:22Z"), ZoneId.of("UTC"));
    }
}
