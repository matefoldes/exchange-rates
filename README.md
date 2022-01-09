# Exchange Rates

Save money and bandwidth by hosting this app and storing data locally. 

This app uses the "**[exchangeratesapi.io](https://exchangeratesapi.io/)**" API to fetch data.

# How does it work

- Currently there are only 2 endpoints of the API is in use: "**latest**" and "**historical**"
  - Latest endpoint
    - When the app starts it fetches the latest rates and stores it on the classpath and memory
    - After a while it refreshes with the latest - it depends on how fresh data you need
  - Historical endpoint
    - When you call it with a new date that isn't stored locally it will fetch from the API then it will update the classpath file with the new data
    - If the historical rates are already there it won't call the API, it will get you the data from the classpath
- There is a switch called "*exchangeratesapi.source*"
  - It can be set to "API" or "CLASSPATH", the default usage is "API"
    - "API": 
      - it tries to fetch data from the API, if it fails to fetch data it will use classpath as fallback
      - fetches latest rates every x minutes
    - "CLASSPATH": 
      - it doesn't call the API at all, only works with classpath files
      - it won't update any historical or latest rates 
    
# How to start the app

- ### Setup how you would like to use it
  - In "com.matefoldes.exchangerates.service.rates.latest.LatestRatesScheduledJob" change the scheduled job delay as how you would like to fetch data
  - In the application.yml file:
    - set "server.port", "exchangeratesapi.source" based on how you would like to use the app.
    - set "exchangeratesapi.api.key" to your own API key.
- ### Start the application
  - Build the application: ```./mvnw clean install```
  - Run the application: ```./mvnw spring-boot:run```

# How to call the API
- latest rates endpoint: ```[yourIP]/latestRates```
- historical rates: ```[yourIP]/historicalRates/[date]```
  - the date should be in this format: "YYYY-MM-DD"
    - example: ```2020-01-25```
    - the format is stored in:
      - ```com.matefoldes.exchangerates.service.rates.historical.HistoricalRatesStore#DATE_FORMAT_PATTERN```

# Code structure

### API
In this package you will find the controllers to your api with the response domain and transformers between response domain and app domain.

### Config
All "@Configuration" class stored here.

### Domain
This package contains all of the domain that is used by the app.

### Exception
Here you can find the ExceptionHandlerController.
It catches unexpected exceptions and returns HTTP 500 with a message if something goes wrong.

### Service
Here you can find service classes for both historical and latest exchnage rates.
You can see how the classpath and API calls are handled here.


# What could be improved?
- Store scheduled job delay time should be moved to application.yaml
- Refactor to a maven multi-module project then create integration tests in another module.
- Exception handling: it should be more precise about what went wrong
- Use another API as fallback to serve even more fresh data
- Use more API endpoints: [API endpoints](https://exchangeratesapi.io/documentation/#endpoints)
- Export DATE format to application.yml and let the format to be changed via config
