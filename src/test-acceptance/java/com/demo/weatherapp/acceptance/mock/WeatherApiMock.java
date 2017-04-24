package com.demo.weatherapp.acceptance.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import gherkin.lexer.Th;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Collections.emptyList;

@Component
public class WeatherApiMock {

    private static final int READ_TIMEOUT = 2000;
    private static final String WEATHER_PATH = "/data/2.5/weather";
    private static final String ACCEPTANCE_TEST_KEY = "acceptance-test-key";
    private static final String APPLICATION_JSON = "application/json";
    private static final int PORT = 8084;

    private final WireMockServer wireMockServer = new WireMockServer(wireMockConfig()
            .port(PORT)
            .recordRequestHeadersForMatching(emptyList()));

    public void prime(String cityname, String response) {
        wireMockServer.stubFor(get(urlPathEqualTo(WEATHER_PATH))
                .withQueryParam("q", new EqualToPattern(cityname))
                .withQueryParam("appid", new EqualToPattern(ACCEPTANCE_TEST_KEY))
                .willReturn(aResponse().withStatus(200).withBody(response).withHeader("Content-Type", APPLICATION_JSON)));
    }

    public synchronized void start() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    public synchronized void stop() {
        wireMockServer.stop();
    }

    public void primeReadTimeout() {
        wireMockServer.stubFor(get(urlPathEqualTo(WEATHER_PATH))
                .willReturn(aResponse().withStatus(200).withFixedDelay(READ_TIMEOUT)));
    }

    public void primeNon200(String city, int non200, String json) {
        wireMockServer.stubFor(get(urlPathEqualTo(WEATHER_PATH))
                .withQueryParam("q", new EqualToPattern(city))
                .withQueryParam("appid", new EqualToPattern(ACCEPTANCE_TEST_KEY))
                .willReturn(aResponse().withStatus(non200).withBody(json)
                        .withHeader("Content-Type", APPLICATION_JSON)));
    }

    public void reset() {
        wireMockServer.resetMappings();
    }
}
