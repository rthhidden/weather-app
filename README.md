# Weather App

Fetching weather details from weather server

## Getting started
Monolithic Application. Technologies used:
 * Java 8
 * Spring Boot (v 1.4.4)
 * Spring Boot Mustache
 * Wiremock (v2.6.0) for stubbing weather api server
 * Cucumber Spring (v 1.2.5) (for writing acceptance tests)
 * Maven (v 3.x)
 * Mockito and Junit
 * Intellij IDEA

## Reasoning behind design
I can see different approaches here: 
* separate UI and Backend Completely. For example basically Backend would return REST and UI could be using e.g. angular js
* monolithic approach use JSP and taglibs in the view templates 
* monolithic approach with light view templating (mustache)

Because having proper UI is not a strict requirement, and also because there is nothing that is driving to separate the UI and backend, I've decided to use the third approach. Also for simplicity reasons (KISS, YAGNY).

## Prerequisite
* Make sure ports 8080 and 8084 are free. `lsof -i:8080` (see TODO section)
* Intellij Maven plugin
* Cucumber for Java Intellij Plugin (if you want to run cucumber tests from Intellij)
* export weather api key env variable before starting the server (see Run the app section) `export WEATHER_API_KEY=1d48a7a2ddf8ab3a52440b372f986181`

## Run tests (Command Line)
Navigate to main directory and type
```
 mvn clean test

```

## Running the tests from within IDE
Unit tests can be run as usually. Nothing special here. However, for running acceptance tests from Intellij you need to install **Cucumber Java** Intellij plugin.
 Then open any .feature file and run as you would run any test. Everything should work fine
  
**Note:** if you get NoClassDefFoundError when you run the test please see this article https://foreach.atlassian.net/wiki/pages/viewpage.action?pageId=9404422
You might have to go to Intellij, Open Edit Configuration and under Defaults -> Cucumber set the glue argument to this **com.demo.weatherapp.acceptance.steps**

## Run the app
For security and maintainability reasons the **weather api key** is not hardcoded in the source files. Make sure you export an environment variable before running the server.
Open a terminal navigate to the project main folder and type following commands:

```
export WEATHER_API_KEY=1d48a7a2ddf8ab3a52440b372f986181
mvn spring-boot:run
 
```
Now navigate to http://localhost:8080/


## TODO List
* use a caching library. As weather does not change very quickly we could consider caching the results returned by the WeatherService. TTL it for a few seconds for example
* run spring container and wiremock server on random free ports. At the moment they run on ports 8080 and 8084.
* validate that if the user sends a request for a location which is not supported then a Bad Request is sent back with an appropriate error message.
* all dates e.g. sunrise sunset are in UTC but maybe re-consider showing them in different timezones.