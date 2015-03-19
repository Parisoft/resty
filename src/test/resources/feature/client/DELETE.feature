#language:en
Feature: DELETE requests

  Scenario: DELETE cars as json response
    When do a DELETE request to "/car" for a Response instance from json
    Then the status code is 200
    And the content type is "application/json"

  Scenario: DELETE cars as json object
    When do a DELETE request to "/car" for a list of Car instances from json
    Then the car list size is 2
    And the 1st car is a "Chevrolet Caprice"
    And the 2nd car is a "Dodge Dart"

  Scenario: DELETE a car as json object
    When do a DELETE request to "/car/dart" for a Car instance from json
    Then the car is a "Dodge Dart"

  Scenario: DELETE cars as xml response
    When do a DELETE request to "/car" for a Response instance from xml
    Then the status code is 200
    And the content type is "application/xml"

  Scenario: DELETE cars as xml object
    When do a DELETE request to "/car" for a list of Car instances from xml
    Then the car list size is 2
    And the 1st car is a "Chevrolet Caprice"
    And the 2nd car is a "Dodge Dart"

  Scenario: DELETE a car as xml object
    When do a DELETE request to "/car/dart" for a Car instance from xml
    Then the car is a "Dodge Dart"
