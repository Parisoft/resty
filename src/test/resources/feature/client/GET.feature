#language:en
Feature: GET requests

  Scenario: GET cars as json response
    When do a GET request to "/car" for a Response instance from json
    Then the status code is 200
    And the content type is "application/json"

  Scenario: GET cars as json object
    When do a GET request to "/car" for a list of Car instances from json
    Then the car list size is 2
    And the 1st car is a "Chevrolet Caprice"
    And the 2nd car is a "Dodge Dart"

  Scenario: GET a car as json object
    When do a GET request to "/car/dart" for a Car instance from json
    Then the car is a "Dodge Dart"

  Scenario: GET cars as xml response
    When do a GET request to "/car" for a Response instance from xml
    Then the status code is 200
    And the content type is "application/xml"

  Scenario: GET cars as xml object
    When do a GET request to "/car" for a list of Car instances from xml
    Then the car list size is 2
    And the 1st car is a "Chevrolet Caprice"
    And the 2nd car is a "Dodge Dart"

  Scenario: GET a car as xml object
    When do a GET request to "/car/dart" for a Car instance from xml
    Then the car is a "Dodge Dart"
