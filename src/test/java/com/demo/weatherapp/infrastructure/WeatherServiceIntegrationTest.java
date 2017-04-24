package com.demo.weatherapp.infrastructure;

import com.demo.weatherapp.config.ApiConfig;
import com.demo.weatherapp.exception.FetchWeatherException;
import com.demo.weatherapp.util.PortScavenger;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.demo.weatherapp.util.TestUtil.bigDecimal;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@RunWith(Theories.class)
public class WeatherServiceIntegrationTest {

    @DataPoints
    public static final List<Integer> non200Codes = Arrays.asList(400, 401, 402, 500, 502, 503, 504);

    private static final String CONTENT_TYPE = "application/json";
    private static final String CITY = "London";
    private static final String TEST_KEY = "test-key";
    private static final int READ_TIMEOUT = 1000;
    private static final String PATH = "/path";

    private final String CONNECT_TIMEOUT_MESSAGE = "Failure talking to weather server - Could not connect";
    private final String READ_TIMEOUT_MESSAGE = "Failure talking to weather server - Read timeout";
    private final String INVALID_CONTENT_MESSAGE = "Failure fetching weather content. Weather server content invalid.";
    private final String STATUS_CODE_ERROR = "Failure fetching weather content. Weather server responded with status %s";


    private ResponseDefinitionBuilder responseBuilder = aResponse()
            .withHeader("Content-Type", CONTENT_TYPE)
            .withStatus(200);

    private MappingBuilder defaultMappingBuilder = get(urlPathEqualTo("/path"))
            .withHeader("Accept", equalTo(CONTENT_TYPE))
            .withQueryParam("q", equalTo(CITY))
            .withQueryParam("appid", equalTo(TEST_KEY));

    @InjectMocks
    private WeatherService subject;

    @Mock
    private ApiConfig apiConfig;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PortScavenger.getFreePort());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(apiConfig.getUrl()).thenReturn(format("http://localhost:%s%s", wireMockRule.port(), PATH));
        when(apiConfig.getKey()).thenReturn(TEST_KEY);
    }

    @Test
    public void shouldReturnCorrectWeatherVO_whenResponseCorrect() {
        stubSuccessWithBody("validResponse.json");

        WeatherVO weatherVO = subject.getWeather(CITY);

        assertThat(weatherVO.getCity()).isEqualTo(Optional.of(CITY));
        assertThat(weatherVO.getTemperatureKelvin()).isEqualTo(Optional.of(bigDecimal("286.23")));
        assertThat(weatherVO.getSunrise()).isEqualTo(Optional.of(Instant.ofEpochSecond(1492750221L)));
        assertThat(weatherVO.getSunset()).isEqualTo(Optional.of(Instant.ofEpochSecond(1492801728L)));
        assertThat(weatherVO.getWeatherDetailsList()).hasSize(2);
        assertThat(weatherVO.getWeatherDetailsList().get(0).getDescription()).isEqualTo("overcast clouds");
        assertThat(weatherVO.getWeatherDetailsList().get(1).getDescription()).isEqualTo("fog");
    }

    @Test
    public void shouldReturnNoSunrise_whenSunriseMissingInTheResponse() {
        stubSuccessWithBody("sunrise-missing.json");

        WeatherVO weatherVO = subject.getWeather(CITY);

        assertThat(weatherVO.getSunrise()).isEmpty();
    }

    @Test
    public void shouldThrowException_whenSunriseInvalidInTheResponse() {
        stubSuccessWithBody("sunrise-invalid.json");

        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));

        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(INVALID_CONTENT_MESSAGE);
    }

    @Test
    public void shouldReturnNoSunset_whenSunsetMissingInTheResponse() {
        stubSuccessWithBody("sunset-missing.json");

        WeatherVO weatherVO = subject.getWeather(CITY);

        assertThat(weatherVO.getSunset()).isEmpty();
    }

    @Test
    public void shouldThrowException_whenSunsetInvalidInTheResponse() {
        stubSuccessWithBody("sunset-invalid.json");

        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));

        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(INVALID_CONTENT_MESSAGE);
    }

    @Test
    public void shouldReturnNoWeatherDetails_whenWeatherDetailsMissingInTheResponse() {
        stubSuccessWithBody("weather-details-missing.json");

        WeatherVO weatherVO = subject.getWeather(CITY);

        assertThat(weatherVO.getWeatherDetailsList()).isEmpty();
    }

    @Test
    public void shouldReturnNoTemperature_whenMainWeatherBlockMissingInTheResponse() {
        stubSuccessWithBody("main-missing.json");

        WeatherVO weatherVO = subject.getWeather(CITY);

        assertThat(weatherVO.getTemperatureKelvin()).isEmpty();
    }

    @Test
    public void shouldReturnNoTemperature_whenMainWeMissingInTheResponse() {
        stubSuccessWithBody("temperature-missing.json");

        WeatherVO weatherVO = subject.getWeather(CITY);

        assertThat(weatherVO.getTemperatureKelvin()).isEmpty();
    }

    @Test
    public void shouldThrowException_whenTemperatureInvalidInTheResponse() {
        stubSuccessWithBody("temperature-invalid.json");

        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));
        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(INVALID_CONTENT_MESSAGE);
    }

    @Test
    public void shouldThrowException_whenServerIsDown() {
        wireMockRule.stop();

        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));

        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(CONNECT_TIMEOUT_MESSAGE);
    }

    @Test
    public void shouldThrowException_whenServerIsNotResponding() {
        when(apiConfig.getReadTimeoutMillis()).thenReturn(READ_TIMEOUT);
        stubbNotResponding();

        subject = new WeatherService(apiConfig);


        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));

        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(READ_TIMEOUT_MESSAGE);
    }

    @Test
    @Theory
    public void shouldThrowException_whenResponseStatusIsNon200(int non200Code) {
        stubNon200(non200Code);

        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));

        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(format(STATUS_CODE_ERROR, non200Code));

    }

    @Theory
    public void shouldThrowException_whenResponseDataInvalid() {
        stubSuccessWithBody("invalid.json");

        Throwable throwable = catchThrowable(() -> subject.getWeather(CITY));

        assertThat(throwable).isInstanceOf(FetchWeatherException.class);
        assertThat(throwable.getMessage()).isEqualTo(INVALID_CONTENT_MESSAGE);
    }

    private void stubSuccessWithBody(String bodyFile) {
        stubFor(defaultMappingBuilder
                .willReturn(responseBuilder
                        .withBodyFile(new ClassPathResource(bodyFile, getClass()).getFilename())
                )
        );
    }

    private void stubNon200(int non200) {
        stubFor(defaultMappingBuilder
                .willReturn(responseBuilder
                        .withStatus(non200))
        );
    }

    private void stubbNotResponding() {
        stubFor(defaultMappingBuilder.willReturn(responseBuilder
                .withFixedDelay(READ_TIMEOUT + 500)));
    }

}