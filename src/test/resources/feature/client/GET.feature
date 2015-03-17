#language:en
Feature: GET requests

  Scenario: GET cars as response
    When do a GET request to server with path <"car"> for a Response instance
    Then the status code is 200
    And the entity string is not empty

  Scenario: GET cars as Object
    When do a GET request to server with path "car" for a List of Car instance
    Then the car list size is 2
    And the cars on list are valid

  Scenario: GET a car as response
    When do a GET request to server with path <car> and path <dart> for a Response instance
    Then the status code is 200
    And the entity string is not empty

  Scenario: GET a car as Object
    When do a GET request to server with path "car" and path "dart" for a Car instance
    Then the car list size is 1
    And the car is a valid "Dodge Dart"
