package com.demo.weatherapp.infrastructure;

import com.demo.weatherapp.config.ApiConfig;
import com.demo.weatherapp.exception.FetchWeatherException;
import org.apache.http.HttpStatus;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;

import static com.demo.weatherapp.infrastructure.ErrorResponse.CONNECT_TIMEOUT;
import static com.demo.weatherapp.infrastructure.ErrorResponse.INVALID_CONTENT;
import static com.demo.weatherapp.infrastructure.ErrorResponse.READ_TIMEOUT;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private static final String QUERY_PARAM = "?q={city}&appid={key}";

    private final ApiConfig apiConfig;
    private final RestTemplate restTemplate;

    public WeatherService(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(apiConfig.getConnectTimeoutMillis())
                .setReadTimeout(apiConfig.getReadTimeoutMillis())
                .build();
        this.restTemplate.setErrorHandler(new WeatherApiResponseErrorHandler());
    }

    public WeatherVO getWeather(String city) {
        try {
            return fetchWeather(city);

        } catch (RestClientException e) {
            throw handleRestException(e);
        } catch (HttpMessageNotReadableException e) {
            throw new FetchWeatherException(INVALID_CONTENT, e.getCause());
        }
    }

    private WeatherVO fetchWeather(String city) {
        URI url = new UriTemplate(apiConfig.getUrl() + QUERY_PARAM).expand(city, apiConfig.getKey());
        RequestEntity<?> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<WeatherVO> exchange = this.restTemplate
                .exchange(request, WeatherVO.class);

        return exchange.getBody();
    }

    private RuntimeException handleRestException(RestClientException e) {
        if (e.getCause() instanceof HttpHostConnectException) {
            throw new FetchWeatherException(CONNECT_TIMEOUT, e);
        }
        if (e.getCause() instanceof SocketTimeoutException) {
            throw new FetchWeatherException(READ_TIMEOUT, e);
        }

        logger.error("exception talking to weather server", e);

        throw e;
    }

    private static class WeatherApiResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getRawStatusCode() != HttpStatus.SC_OK;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            throw new FetchWeatherException(String.format(ErrorResponse.STATUS_CODE_ERROR, response.getRawStatusCode()), null);
        }
    }
}
