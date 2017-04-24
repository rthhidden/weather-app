package com.demo.weatherapp;

import com.demo.weatherapp.config.WeatherAppConfig;
import com.demo.weatherapp.infrastructure.WeatherService;
import com.demo.weatherapp.infrastructure.WeatherVO;
import com.demo.weatherapp.infrastructure.WeatherVO.WeatherDetailsVO;
import com.demo.weatherapp.facade.WeatherDTO;
import com.demo.weatherapp.facade.WeatherFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.*;
import java.util.Collections;
import java.util.Optional;

import static com.demo.weatherapp.util.TestUtil.bigDecimal;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherFacadeTest {

    private static final String CITY = "London";
    private static final Instant SUNRISE = Instant.parse("2014-12-03T07:15:30Z");
    private static final Instant SUNSET = Instant.parse("2014-12-03T19:00:30Z");
    private static final BigDecimal TEMP_KELVIN = bigDecimal("286.23");
    private static final LocalDate TODAY_DATE = LocalDate.of(2017, 4, 5);
    private static final String SUNRISE_SUNSET_FORMAT_CONFIG = "h:mm a";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String DATE_FORMAT_CONFIG = DATE_FORMAT;
    private static final String NOT_AVAILABLE = "N/A";
    private static final String OVERCAST_CLOUDS = "overcast clouds";
    private static final String FOG = "fog";

    @Mock
    private WeatherService weatherService;

    @Mock
    private WeatherVO weatherVO;

    @Mock
    private WeatherAppConfig config;

    private WeatherFacade subject;

    private Clock clock = Clock.fixed(TODAY_DATE.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));

    @Before
    public void setup() {
        when(config.getDateFormat()).thenReturn(DATE_FORMAT_CONFIG);
        when(config.getSunriseSunsetDateFormat()).thenReturn(SUNRISE_SUNSET_FORMAT_CONFIG);

        subject = new WeatherFacade(weatherService, config, clock);

        givenDefaultWeatherVO();
    }

    @Test
    public void shouldReturnCorrectWeatherDetails() {

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getCity()).isEqualTo(CITY);
        assertThat(weather.getTodayDate()).isEqualTo("05-04-2017");
        assertThat(weather.getTemperatureCelsius()).isEqualTo("13.08");
        assertThat(weather.getTemperatureFahrenheit()).isEqualTo("55.54");
        assertThat(weather.getSunrise()).isEqualTo("7:15 AM");
        assertThat(weather.getSunset()).isEqualTo("7:00 PM");
        assertThat(weather.getWeatherDescription()).isEqualTo("overcast clouds,fog");
    }

    @Test
    public void shouldReturnCityNotAvailable_whenCityIsMissing() {
        when(weatherVO.getCity()).thenReturn(Optional.empty());

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getCity()).isEqualTo(NOT_AVAILABLE);
    }

    @Test
    public void shouldReturnTemperaturesNotAvailable_whenTemperatureIsMissing() {
        when(weatherVO.getTemperatureKelvin()).thenReturn(Optional.empty());

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getTemperatureCelsius()).isEqualTo(NOT_AVAILABLE);
        assertThat(weather.getTemperatureFahrenheit()).isEqualTo(NOT_AVAILABLE);
    }

    @Test
    public void shouldReturnSunriseNotAvailable_whenSunriseIsMissing() {
        when(weatherVO.getSunrise()).thenReturn(Optional.empty());

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getSunrise()).isEqualTo(NOT_AVAILABLE);
    }

    @Test
    public void shouldReturnSunsetNotAvailable_whenSunsetIsMissing() {
        when(weatherVO.getSunset()).thenReturn(Optional.empty());

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getSunset()).isEqualTo(NOT_AVAILABLE);
    }

    @Test
    public void shouldReturnWeatherDescriptionNotAvailable_whenDeascriptionIsMissing() {
        WeatherDetailsVO nullDescription1 = mock(WeatherDetailsVO.class);
        when(nullDescription1.getDescription()).thenReturn(null);

        WeatherDetailsVO nullDescription2 = mock(WeatherDetailsVO.class);
        when(nullDescription2.getDescription()).thenReturn(null);

        when(weatherVO.getWeatherDetailsList()).thenReturn(asList(nullDescription1, nullDescription2));
        when(weatherService.getWeather(CITY)).thenReturn(weatherVO);

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getWeatherDescription()).isEqualTo(NOT_AVAILABLE);
    }

    @Test
    public void shouldReturnOnlyNonNullWeatherDescriptions() {
        WeatherDetailsVO clouds = mock(WeatherDetailsVO.class);
        when(clouds.getDescription()).thenReturn("overcast clouds");

        WeatherDetailsVO nullDescription = mock(WeatherDetailsVO.class);
        when(nullDescription.getDescription()).thenReturn(null);

        when(weatherVO.getWeatherDetailsList()).thenReturn(asList(clouds, nullDescription));
        when(weatherService.getWeather(CITY)).thenReturn(weatherVO);

        WeatherDTO weather = subject.getWeather(CITY);

        assertThat(weather.getWeatherDescription()).isEqualTo("overcast clouds");
    }

    private void givenDefaultWeatherVO() {
        when(weatherVO.getCity()).thenReturn(Optional.of(CITY));
        when(weatherVO.getTemperatureKelvin()).thenReturn(Optional.of(TEMP_KELVIN));
        when(weatherVO.getSunrise()).thenReturn(Optional.of(SUNRISE));
        when(weatherVO.getSunset()).thenReturn(Optional.of(SUNSET));
        WeatherDetailsVO clouds = mock(WeatherDetailsVO.class);
        WeatherDetailsVO fog = mock(WeatherDetailsVO.class);
        when(clouds.getDescription()).thenReturn(OVERCAST_CLOUDS);
        when(fog.getDescription()).thenReturn(FOG);
        when(weatherVO.getWeatherDetailsList()).thenReturn(asList(clouds, fog));
        when(weatherService.getWeather(CITY)).thenReturn(weatherVO);
    }

}