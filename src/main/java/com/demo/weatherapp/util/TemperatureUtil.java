package com.demo.weatherapp.util;

import java.math.BigDecimal;

public class TemperatureUtil {

    private static final BigDecimal REFERENCE = new BigDecimal("273.15");

    public static BigDecimal getCelsiusFromKelvin(BigDecimal kelvin) {
        return kelvin.subtract(REFERENCE);
    }

    public static BigDecimal getFahrenheitFromKelvin(BigDecimal kelvin) {
        return kelvin.multiply(new BigDecimal("1.8")).subtract(new BigDecimal("459.67"));
    }

}
