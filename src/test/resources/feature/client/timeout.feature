#language:en
Feature: Timeout

  Scenario: Timeout occurs
    Given a timeout of 200 millis
    When do a HEAD request to "/sleep/700" with the given timeout
    Then a SoketTimeoutException is thrown
    And the time of request is greater then the given timeout

  Scenario: Timeout dont occurs
    Given a timeout of 700 millis
    When do a HEAD request to "/sleep/200" with the given timeout
    Then the response status code is 204
    And the time of request is lower then the given timeout
