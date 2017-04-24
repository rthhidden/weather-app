Feature: failures retrieving weather

  Scenario: weather api server is down
    Given weather api is down
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure talking to weather server - Could not connect</div> |

  Scenario: weather api server not responding
    Given weather api is not responding
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure talking to weather server - Read timeout</div> |

  Scenario Outline: weather api server returns non 200
    Given weather api for "London" returns status code "<non200>" and json:
    """
      {
        "errorCode" : "SYS_6004",
        "reason" : "Unknown Error"
      }
    """
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure fetching weather content. Weather server responded with status <non200></div> |
    Examples:
      | non200 |
      | 201    |
      | 400    |
      | 402    |
      | 402    |
      | 500    |
      | 503    |

  Scenario: weather api server returns invalid json
    Given weather api returns following data for "London":
    """
      {
        "Invalid json"
      }
    """
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure fetching weather content. Weather server content invalid.</div> |

  Scenario: weather api server returns invalid sunrise
    Given weather api returns following data for "London":
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
	"sys": {"type": 1,"id": 7904,"message": 0.0029,"country": "HK",

	  "sunrise": "rubish","sunset": 1492944433

	},

	"id": 1818209,
	"name": "Tsuen Wan",
	"cod": 200
    }

    """
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure fetching weather content. Weather server content invalid.</div> |

  Scenario: weather api server returns invalid sunset
    Given weather api returns following data for "London":
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
	"sys": {"type": 1,"id": 7904,"message": 0.0029,"country": "HK","sunrise": 1492898196,

	    "sunset": "rubbsoh"
	},


	"id": 1818209,
	"name": "Tsuen Wan",
	"cod": 200
    }
    """
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure fetching weather content. Weather server content invalid.</div> |

  Scenario: weather api server returns invalid temp
    Given weather api returns following data for "London":
    """
    {
	"coord": {"lon": 114.1,"lat": 22.37},
	"weather": [
	    {"id": 701,"main": "Mist","description": "mist","icon": "50n"},
	    {"id": 520,"main": "Rain","description": "light intensity shower rain","icon": "09n"}
	],
	"base": "stations",
	"main": {

	"temp": "rubbish",

	"pressure": 1014, "humidity": 88, "temp_min": 293.15,"temp_max": 294.15},
	"visibility": 7000,
	"wind": {"speed": 4.1,"deg": 80},
	"clouds": {"all": 40},
	"dt": 1492986600,
	"sys": {"type": 1,"id": 7904,"message": 0.0029,"country": "HK","sunrise": 1492898196,"sunset": 1492898196},
	"id": 1818209,
	"name": "Tsuen Wan",
	"cod": 200
    }
    """
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure fetching weather content. Weather server content invalid.</div> |

  Scenario: web server 404 is handle
    When I request page "/unknown"
    Then status code is "404"
    And following details are returned
      | Oops, page not found |

  Scenario: weather api server responds with 200 but no content
    Given weather api responds with no content
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Weather server replied with no content</div> |

  Scenario: weather api server responds with html
    Given weather api responds with html
    When want to fetch weather for "London"
    Then status code is "500"
    And following details are returned
      | <div>Failure fetching weather content</div> |