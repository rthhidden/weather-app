Feature: retrieve weather for a given city

  Scenario: Home page contains the locations and correct form
    When I request home page
    Then status code is "200"
    And following details are returned
      | <form action="weather" method="POST"> |
      | <option>London</option>               |
      | <option>Hong Kong</option>            |

  Scenario Outline: Show details of the weather in London
    Given weather api returns following data for "<city>":
    """
      {
        "coord":{"lon":-0.13,"lat":51.51},
        "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],
        "base":"stations",
        "main":{"temp":286.23,"pressure":1029,"humidity":58,"temp_min":285.15,"temp_max":287.15},
        "visibility":10000,
        "wind":{"speed":5.1,"deg":300},
        "clouds":{"all":90},
        "dt":1492798800,
        "sys":{"type":1,"id":5091,"message":0.0498,"country":"GB","sunrise":1492750221,"sunset":1492801728},
        "id":2643743,
        "name":"<city>",
        "cod":200
       }
    """
    When want to fetch weather for "<city>"
    Then status code is "200"
    And following details are returned
      | <li>City-London</li>                   |
      | <li>Date-09-02-2017</li>               |
      | <li>Weather-overcast clouds</li>       |
      | <li>Temperature(Celsius)-13.08</li>    |
      | <li>Temperature(Fahrenheit)-55.54</li> |
      | <li>Sunrise-4:50 AM</li>               |
      | <li>Sunset-7:08 PM</li>                |
    Examples:
      | city   |
      | London |


  Scenario Outline: Show details of the weather in Honk Kong
    Given weather api returns following data for "<city>":
    """
    {
	"coord": {"lon": 114.1,"lat": 22.37},
	"weather": [
	    {"id": 701,"main": "Mist","description": "mist","icon": "50n"},
	    {"id": 520,"main": "Rain","description": "light intensity shower rain","icon": "09n"}
	],
	"base": "stations",
	"main": {"temp": 293.59, "pressure": 1014, "humidity": 88, "temp_min": 293.15,"temp_max": 294.15},
	"visibility": 7000,
	"wind": {"speed": 4.1,"deg": 80},
	"clouds": {"all": 40},
	"dt": 1492986600,
	"sys": {"type": 1,"id": 7904,"message": 0.0029,"country": "HK","sunrise": 1492898196,"sunset": 1492944433},
	"id": 1818209,
	"name": "Tsuen Wan",
	"cod": 200
    }
    """
    When want to fetch weather for "<city>"
    Then status code is "200"
    And following details are returned
      | <li>City-Tsuen Wan</li>                           |
      | <li>Date-09-02-2017</li>                          |
      | <li>Weather-mist,light intensity shower rain</li> |
      | <li>Temperature(Celsius)-20.44</li>               |
      | <li>Temperature(Fahrenheit)-68.79</li>            |
      | <li>Sunrise-9:56 PM</li>                          |
      | <li>Sunset-10:47 AM</li>                          |
    Examples:
      | city      |
      | Honk Kong |


  Scenario Outline: Show details of the weather in London some attributes missing in the response
    Given weather api returns following data for "<city>":
    """
      {
        "coord":{"lon":-0.13,"lat":51.51},
        "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],
        "base":"stations",
        "main":{"pressure":1029,"humidity":58,"temp_min":285.15,"temp_max":287.15},
        "visibility":10000,
        "wind":{"speed":5.1,"deg":300},
        "clouds":{"all":90},
        "dt":1492798800,
        "sys":{"type":1,"id":5091,"message":0.0498,"country":"GB","sunset":1492801728},
        "id":2643743,
        "name":"<city>",
        "cod":200
       }
    """
    When want to fetch weather for "<city>"
    Then status code is "200"
    And following details are returned
      | <li>City-London</li>                 |
      | <li>Date-09-02-2017</li>             |
      | <li>Weather-overcast clouds</li>     |
      | <li>Temperature(Celsius)-N/A</li>    |
      | <li>Temperature(Fahrenheit)-N/A</li> |
      | <li>Sunrise-N/A</li>                 |
      | <li>Sunset-7:08 PM</li>              |
    Examples:
      | city   |
      | London |
