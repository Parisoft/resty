#language:en
Feature: Timeout

  Scenario: No retry
    Given a timeout of 500 millis
    And a retry value of 0 attempts
    And a request counter on 0
    When do a HEAD request to "/sleep/700" with the given timeout and retry
    Then a SoketTimeoutException is thrown
    And the request counter is 1

  Scenario: Default retry
    Given a timeout of 500 millis
    And a request counter on 0
    When do a HEAD request to "/sleep/700" with the given timeout
    Then a SoketTimeoutException is thrown
    And the request counter is 1

  Scenario: Some retries
    Given a timeout of 500 millis
    And a retry value of 2 attempts
    And a request counter on 0
    When do a HEAD request to "/sleep/700" with the given timeout and retry
    Then a SoketTimeoutException is thrown
    And the request counter is 3
