package com.demo.weatherapp.util;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");

    public static Instant secondsToInstant(String seconds) throws NumberFormatException, DateTimeException {
        return Instant.ofEpochSecond(Long.parseLong(seconds));
    }

    public static Date now(Clock clock) {
        Instant now = Instant.now(clock);
        return Date.from(now);
    }

    public static String format(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat.format(date);
    }

    public static String format(String format, Instant instant) {
        return format(format, Date.from(instant));
    }
}
