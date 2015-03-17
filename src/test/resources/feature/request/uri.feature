#language:en
Feature: Request.path

  Scenario Outline: Normalize URI
    When a request is created with <URI>
    Then the URI from HTTP request is <expectedURI>

    Examples: 
      | URI                              | expectedURI                   |
      | "http://some.domain.com"         | "http://some.domain.com"      |
      | "http://some.domain.com/"        | "http://some.domain.com/"     |
      | "http://some.domain.com//"       | "http://some.domain.com/"     |
      | "http://some.domain.com/a"       | "http://some.domain.com/a"    |
      | "http://some.domain.com/a/"      | "http://some.domain.com/a/"   |
      | "http://some.domain.com/a/b"     | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/a/b/"    | "http://some.domain.com/a/b/" |
      | "http://some.domain.com//a"      | "http://some.domain.com/a"    |
      | "http://some.domain.com//a/"     | "http://some.domain.com/a/"   |
      | "http://some.domain.com//a/b"    | "http://some.domain.com/a/b"  |
      | "http://some.domain.com//a/b/"   | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/a//b"    | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/a//b/"   | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/a/b//"   | "http://some.domain.com/a/b/" |
      | "http://some.domain.com//a//b"   | "http://some.domain.com/a/b"  |
      | "http://some.domain.com//a//b/"  | "http://some.domain.com/a/b/" |
      | "http://some.domain.com//a//b//" | "http://some.domain.com/a/b/" |

  Scenario Outline: Normalize URI and paths
    When a request is created from <URI> and <paths>
    Then the URI from HTTP request is <expectedURI>

    Examples: 
      | URI                       | paths       | expectedURI                   |
      | "http://some.domain.com"  |             | "http://some.domain.com"      |
      | "http://some.domain.com"  | "/"         | "http://some.domain.com/"     |
      | "http://some.domain.com"  | "//"        | "http://some.domain.com/"     |
      | "http://some.domain.com"  | "a"         | "http://some.domain.com/a"    |
      | "http://some.domain.com"  | "/a"        | "http://some.domain.com/a"    |
      | "http://some.domain.com"  | "/,a"       | "http://some.domain.com/a"    |
      | "http://some.domain.com"  | "a,b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"  | "a/b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"  | "/a/b"      | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"  | "/,a,/,b"   | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"  | "a/"        | "http://some.domain.com/a/"   |
      | "http://some.domain.com"  | "/a/"       | "http://some.domain.com/a/"   |
      | "http://some.domain.com"  | "/,a/"      | "http://some.domain.com/a/"   |
      | "http://some.domain.com"  | "a/,b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"  | "a/b/"      | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"  | "/a/b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"  | "/,a/,/,b/" | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/" |             | "http://some.domain.com/"     |
      | "http://some.domain.com/" | "/"         | "http://some.domain.com/"     |
      | "http://some.domain.com/" | "//"        | "http://some.domain.com/"     |
      | "http://some.domain.com/" | "a"         | "http://some.domain.com/a"    |
      | "http://some.domain.com/" | "/a"        | "http://some.domain.com/a"    |
      | "http://some.domain.com/" | "/,a"       | "http://some.domain.com/a"    |
      | "http://some.domain.com/" | "a,b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/" | "a/b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/" | "/a/b"      | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/" | "/,a,/,b"   | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/" | "a/"        | "http://some.domain.com/a/"   |
      | "http://some.domain.com/" | "/a/"       | "http://some.domain.com/a/"   |
      | "http://some.domain.com/" | "/,a/"      | "http://some.domain.com/a/"   |
      | "http://some.domain.com/" | "a/,b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/" | "a/b/"      | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/" | "/a/b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/" | "/,a/,/,b/" | "http://some.domain.com/a/b/" |

  Scenario Outline: Encode URI
    When a request is created from <URI> and <paths>
    Then the URI from HTTP request is <expectedURI>

    Examples: 
      | URI                      | paths          | expectedURI                               |
      | "http://some.domain.com" | "1"            | "http://some.domain.com/1"                |
      | "http://some.domain.com" | "c#"           | "http://some.domain.com/c%23"             |
      | "http://some.domain.com" | "a space"      | "http://some.domain.com/a%20space"        |
      | "http://some.domain.com" | "1/a space/c#" | "http://some.domain.com/1/a%20space/c%23" |
