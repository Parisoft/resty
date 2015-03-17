#language:en
Feature: Request.path

  Scenario Outline: Setting request path with URI
    When a request is created from <URI> and <paths>
    Then the paths from request are <expectedPaths>

    Examples: 
      | URI                       | paths     | expectedPaths |
      | "http://some.domain.com"  |           |               |
      | "http://some.domain.com"  | "/"       | ""            |
      | "http://some.domain.com"  | "//"      | ""            |
      | "http://some.domain.com"  | "a"       | "a"           |
      | "http://some.domain.com"  | "/a"      | "a"           |
      | "http://some.domain.com"  | "/,a"     | "a"           |
      | "http://some.domain.com"  | "a,b"     | "a,b"         |
      | "http://some.domain.com"  | "a/b"     | "a,b"         |
      | "http://some.domain.com"  | "/a/b"    | "a,b"         |
      | "http://some.domain.com"  | "/,a,/,b" | "a,b"         |
      | "http://some.domain.com/" |           | ""            |
      | "http://some.domain.com/" | "/"       | ""            |
      | "http://some.domain.com/" | "//"      | ""            |
      | "http://some.domain.com/" | "a"       | "a"           |
      | "http://some.domain.com/" | "/a"      | "a"           |
      | "http://some.domain.com/" | "/,a"     | "a"           |
      | "http://some.domain.com/" | "a,b"     | "a,b"         |
      | "http://some.domain.com/" | "a/b"     | "a,b"         |
      | "http://some.domain.com/" | "/a/b"    | "a,b"         |
      | "http://some.domain.com/" | "/,a,/,b" | "a,b"         |

  Scenario Outline: Setting request path from URI
    When a request is created with <URI>
    Then the paths from request are <expectedPaths>

    Examples: 
      | URI                            | expectedPaths |
      | "http://some.domain.com"       |               |
      | "http://some.domain.com/"      | ""            |
      | "http://some.domain.com//"     | ""            |
      | "http://some.domain.com/a"     | "a"           |
      | "http://some.domain.com/a/b"   | "a,b"         |
      | "http://some.domain.com//a"    | "a"           |
      | "http://some.domain.com//a/b"  | "a,b"         |
      | "http://some.domain.com/a//b"  | "a,b"         |
      | "http://some.domain.com//a//b" | "a,b"         |
