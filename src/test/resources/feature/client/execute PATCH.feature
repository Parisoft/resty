#language:en
Feature: execute PATCH requests

  Scenario: execute PATCH cars as json response
    Given an entity as a "Ford Fairlane" and a "Buick GSX" list
    When do a execute PATCH request to "/car" for a Response instance from json
    Then the status code is 201
    And the content type is "application/json"

  Scenario: execute PATCH cars as json object
    Given an entity as a "Ford Fairlane" and a "Buick GSX" list
    When do a execute PATCH request to "/car" for a list of Car instances from json
    Then the car list size is 2
    And the 1st car is a "Ford Fairlane"
    And the 2nd car is a "Buick GSX"

  Scenario: execute PATCH a car as json object
    Given an entity as a "Ford Fairlane" instance
    When do a execute PATCH request to "/car/fairlane" for a Car instance from json
    Then the car is a "Ford Fairlane"

  Scenario: execute PATCH cars as xml response
    Given an entity as a "Ford Fairlane" and a "Buick GSX" list
    When do a execute PATCH request to "/car" for a Response instance from xml
    Then the status code is 201
    And the content type is "application/xml"

  Scenario: execute PATCH cars as xml object
    Given an entity as a "Ford Fairlane" and a "Buick GSX" list
    When do a execute PATCH request to "/car" for a list of Car instances from xml
    Then the car list size is 2
    And the 1st car is a "Ford Fairlane"
    And the 2nd car is a "Buick GSX"

  Scenario: execute PATCH a car as xml object
    Given an entity as a "Ford Fairlane" instance
    When do a execute PATCH request to "/car/fairlane" for a Car instance from xml
    Then the car is a "Ford Fairlane"
