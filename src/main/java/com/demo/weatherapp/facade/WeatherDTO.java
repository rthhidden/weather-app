package com.demo.weatherapp.facade;

public class WeatherDTO {

    private final String city;
    private final String todayDate;
    private final String temperatureCelsius;
    private final String temperatureFahrenheit;
    private final String weatherDescription;
    private final String sunrise;
    private final String sunset;

    private WeatherDTO(
            String city,
            String todayDate,
            String temperatureCelsius,
            String temperatureFahrenheit,
            String weatherDescription,
            String sunrise,
            String sunset) {

        this.city = city;
        this.todayDate = todayDate;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = temperatureFahrenheit;
        this.weatherDescription = weatherDescription;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getCity() {
        return city;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public String getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public String getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public static class WeatherDTOBuilder {
        private String city;
        private String todayDate;
        private String temperatureCelsius;
        private String temperatureFahrenheit;
        private String weatherDescription;
        private String sunrise;
        private String sunset;

        public WeatherDTOBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public WeatherDTOBuilder withTodayDate(String todayDate) {
            this.todayDate = todayDate;
            return this;
        }

        public WeatherDTOBuilder withTemperatureCelsius(String temperatureCelsius) {
            this.temperatureCelsius = temperatureCelsius;
            return this;
        }

        public WeatherDTOBuilder withTemperatureFahrenheit(String temperatureFahrenheit) {
            this.temperatureFahrenheit = temperatureFahrenheit;
            return this;
        }

        public WeatherDTOBuilder withWeatherDescription(String weatherDescription) {
            this.weatherDescription = weatherDescription;
            return this;
        }

        public WeatherDTOBuilder withSunset(String sunset) {
            this.sunset = sunset;
            return this;
        }

        public WeatherDTOBuilder withSunrise(String sunrise) {
            this.sunrise = sunrise;
            return this;
        }

        public WeatherDTO build() {
            return new WeatherDTO(city, todayDate, temperatureCelsius, temperatureFahrenheit, weatherDescription, sunrise, sunset);
        }
    }
}
