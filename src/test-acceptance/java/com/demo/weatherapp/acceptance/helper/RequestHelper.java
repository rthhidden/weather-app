package com.demo.weatherapp.acceptance.helper;

import com.demo.weatherapp.infrastructure.WeatherVO;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;

@Component
public class RequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

    private static final String WEBSERVER_URL = "http://localhost:8080";

    private ResponseEntity<String> exchange;

    private final ResponseErrorHandler responseErrorHandler;

    public RequestHelper() {
        responseErrorHandler = new ResponseHandler();
    }

    public void postWeatherData(String cityName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("cityName", cityName);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        RestTemplate restTemplate = getRestTemplate();

        exchange = restTemplate.postForEntity(getFullUrl("/weather"), request, String.class);
    }

    public void requestHomePage() {
        request("/");
    }

    public void request(String url) {
        URI uri = new UriTemplate(getFullUrl(url)).expand();
        RequestEntity<?> request = RequestEntity.get(uri)
                .accept(MediaType.TEXT_HTML).build();
        this.exchange = getRestTemplate()
                .exchange(request, String.class);
    }

    public String getBody() {
        return exchange.getBody();
    }

    public int getStatusCode() {
        return exchange.getStatusCodeValue();
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

    private String getFullUrl(String url) {
        return WEBSERVER_URL + url;
    }

    private static class ResponseHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            return clientHttpResponse.getRawStatusCode() != HttpStatus.SC_OK;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            logger.error("error http in acceptance test ", clientHttpResponse.getRawStatusCode());
        }
    }
}
