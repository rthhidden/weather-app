package com.demo.weatherapp;

import com.demo.weatherapp.exception.FetchWeatherException;
import com.demo.weatherapp.facade.WeatherDTO;
import com.demo.weatherapp.facade.WeatherDTO.WeatherDTOBuilder;
import com.demo.weatherapp.facade.WeatherFacade;
import com.demo.weatherapp.web.WeatherController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
@TestPropertySource(properties = "weather.locations=London,Test-location")
public class WeatherControllerTest {

    private static final String CITY = "London";
    private static final String WEATHER_DETAILS_PAGE = "weather-details";
    private static final String WEATHER_ERROR_PAGE = "weather-error";
    private static final String HOME_PAGE = "home";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherFacade weatherFacade;

    private WeatherDTO weatherDTO = new WeatherDTOBuilder()
            .withCity(CITY)
            .withWeatherDescription("rain")
            .withTodayDate("03-04-2017")
            .withSunrise("9:45 AM")
            .withSunset("10:45 AM")
            .withTemperatureCelsius("21.3")
            .withTemperatureFahrenheit("20.3")
            .build();

    @Test
    public void shouldShowAvailableLocationsOnHomePage() throws Exception {
        ModelAndView modelAndView = this.mvc.perform(
                get("/").accept(MediaType.TEXT_HTML)
        ).andExpect(status().isOk()).andReturn().getModelAndView();

        Map<String, Object> model = modelAndView.getModel();
        Object locationsObj = model.get("locations");
        assertThat(locationsObj).isEqualTo(Arrays.asList("London", "Test-location"));
        assertThat(modelAndView.getViewName()).isEqualTo(HOME_PAGE);
    }

    @Test
    public void shouldReturnWeatherDetailsOnPostRequest() throws Exception {
        given(weatherFacade.getWeather(CITY)).willReturn(weatherDTO);

        ModelAndView modelAndView = this.mvc.perform(
                post("/weather")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("cityName", CITY)
        ).andExpect(status().isOk()).andReturn().getModelAndView();

        Map<String, Object> model = modelAndView.getModel();
        WeatherDTO actualWeatherDTO = (WeatherDTO) model.get("weather");
        assertThat(actualWeatherDTO).isSameAs(weatherDTO);

        assertThat(modelAndView.getViewName()).isEqualTo(WEATHER_DETAILS_PAGE);
    }

    @Test
    public void shouldHandleWeatherException() throws Exception {
        FetchWeatherException exception = new FetchWeatherException("error", null);
        when(this.weatherFacade.getWeather(CITY)).thenThrow(exception);

        ModelAndView modelAndView = this.mvc.perform(
                post("/weather")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("cityName", CITY)
        ).andExpect(status().is5xxServerError()).andReturn().getModelAndView();

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model.get("exception")).isSameAs(exception);
        assertThat(model.get("reason")).isEqualTo("error");
        assertThat(modelAndView.getViewName()).isEqualTo(WEATHER_ERROR_PAGE);
    }

}