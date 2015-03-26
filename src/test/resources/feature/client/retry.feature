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

  Scenario: No retry for POST
    Given a timeout of 500 millis
    And a retry value of 1 attempts
    And a request counter on 0
    When do a POST request to "/sleep/700" with the given timeout and retry
    Then a SoketTimeoutException is thrown
    And the request counter is 1

  Scenario: Retry for POST as idempotent
    Given a timeout of 500 millis
    And a retry value of 1 attempts
    And a request counter on 0
    And the "POST" method as idempotent
    When do a POST request to "/sleep/700" with the given timeout and retry
    Then a SoketTimeoutException is thrown
    And the request counter is 2

  Scenario: No retry for HEAD as no idempotent
    Given a timeout of 500 millis
    And a retry value of 1 attempts
    And a request counter on 0
    And the "HEAD" method as no idempotent
    When do a POST request to "/sleep/700" with the given timeout and retry
    Then a SoketTimeoutException is thrown
    And the request counter is 1
