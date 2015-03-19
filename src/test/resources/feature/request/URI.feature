#language:en
Feature: Request URI

  Scenario Outline: Normalize URI
    When a request is created from <URI> and <paths>
    Then the URI from HTTP request is <expectedURI>

    Examples: 
      | URI                              | paths       | expectedURI                   |
      | "http://some.domain.com"         |             | "http://some.domain.com"      |
      | "http://some.domain.com/"        |             | "http://some.domain.com/"     |
      | "http://some.domain.com//"       |             | "http://some.domain.com/"     |
      | "http://some.domain.com/a"       |             | "http://some.domain.com/a"    |
      | "http://some.domain.com/a/"      |             | "http://some.domain.com/a/"   |
      | "http://some.domain.com/a/b"     |             | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/a/b/"    |             | "http://some.domain.com/a/b/" |
      | "http://some.domain.com//a"      |             | "http://some.domain.com/a"    |
      | "http://some.domain.com//a/"     |             | "http://some.domain.com/a/"   |
      | "http://some.domain.com//a/b"    |             | "http://some.domain.com/a/b"  |
      | "http://some.domain.com//a/b/"   |             | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/a//b"    |             | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/a//b/"   |             | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/a/b//"   |             | "http://some.domain.com/a/b/" |
      | "http://some.domain.com//a//b"   |             | "http://some.domain.com/a/b"  |
      | "http://some.domain.com//a//b/"  |             | "http://some.domain.com/a/b/" |
      | "http://some.domain.com//a//b//" |             | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"         | "/"         | "http://some.domain.com/"     |
      | "http://some.domain.com"         | "//"        | "http://some.domain.com/"     |
      | "http://some.domain.com"         | "a"         | "http://some.domain.com/a"    |
      | "http://some.domain.com"         | "/a"        | "http://some.domain.com/a"    |
      | "http://some.domain.com"         | "/,a"       | "http://some.domain.com/a"    |
      | "http://some.domain.com"         | "a,b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"         | "a/b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"         | "/a/b"      | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"         | "/,a,/,b"   | "http://some.domain.com/a/b"  |
      | "http://some.domain.com"         | "a/"        | "http://some.domain.com/a/"   |
      | "http://some.domain.com"         | "/a/"       | "http://some.domain.com/a/"   |
      | "http://some.domain.com"         | "/,a/"      | "http://some.domain.com/a/"   |
      | "http://some.domain.com"         | "a/,b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"         | "a/b/"      | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"         | "/a/b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com"         | "/,a/,/,b/" | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/"        | "/"         | "http://some.domain.com/"     |
      | "http://some.domain.com/"        | "//"        | "http://some.domain.com/"     |
      | "http://some.domain.com/"        | "a"         | "http://some.domain.com/a"    |
      | "http://some.domain.com/"        | "/a"        | "http://some.domain.com/a"    |
      | "http://some.domain.com/"        | "/,a"       | "http://some.domain.com/a"    |
      | "http://some.domain.com/"        | "a,b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/"        | "a/b"       | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/"        | "/a/b"      | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/"        | "/,a,/,b"   | "http://some.domain.com/a/b"  |
      | "http://some.domain.com/"        | "a/"        | "http://some.domain.com/a/"   |
      | "http://some.domain.com/"        | "/a/"       | "http://some.domain.com/a/"   |
      | "http://some.domain.com/"        | "/,a/"      | "http://some.domain.com/a/"   |
      | "http://some.domain.com/"        | "a/,b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/"        | "a/b/"      | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/"        | "/a/b/"     | "http://some.domain.com/a/b/" |
      | "http://some.domain.com/"        | "/,a/,/,b/" | "http://some.domain.com/a/b/" |

  Scenario Outline: Encode URI path
    When a request is created from <URI> and <paths>
    Then the URI from HTTP request is <expectedURI>

    Examples: 
      | URI                                       | paths          | expectedURI                               |
      | "http://some.domain.com/1"                |                | "http://some.domain.com/1"                |
      | "http://some.domain.com/c%23"             |                | "http://some.domain.com/c%23"             |
      | "http://some.domain.com/a%20space"        |                | "http://some.domain.com/a%20space"        |
      | "http://some.domain.com/1/a%20space/c%23" |                | "http://some.domain.com/1/a%20space/c%23" |
      | "http://some.domain.com"                  | "1"            | "http://some.domain.com/1"                |
      | "http://some.domain.com"                  | "c#"           | "http://some.domain.com/c%23"             |
      | "http://some.domain.com"                  | "a space"      | "http://some.domain.com/a%20space"        |
      | "http://some.domain.com"                  | "1/a space/c#" | "http://some.domain.com/1/a%20space/c%23" |

  Scenario Outline: Encode URI query
    When a request is created with <URI> and <queries>
    Then the URI from HTTP request is <expectedURI>

    Examples: 
      | URI                                           | queries        | expectedURI                                   |
      | "http://some.domain.com?a=1"                  |                | "http://some.domain.com?a=1"                  |
      | "http://some.domain.com/?a=1"                 |                | "http://some.domain.com/?a=1"                 |
      | "http://some.domain.com/p?a=1"                |                | "http://some.domain.com/p?a=1"                |
      | "http://some.domain.com?a=1&b=2"              |                | "http://some.domain.com?a=1&b=2"              |
      | "http://some.domain.com/?a=1&b=2"             |                | "http://some.domain.com/?a=1&b=2"             |
      | "http://some.domain.com/p?a=1&b=2"            |                | "http://some.domain.com/p?a=1&b=2"            |
      | "http://some.domain.com?c%23=c%2B%2B"         |                | "http://some.domain.com?c%23=c%2B%2B"         |
      | "http://some.domain.com?c%23=c%2B%2B&a+a=%40" |                | "http://some.domain.com?c%23=c%2B%2B&a+a=%40" |
      | "http://some.domain.com"                      | "a=1"          | "http://some.domain.com?a=1"                  |
      | "http://some.domain.com/"                     | "a=1"          | "http://some.domain.com/?a=1"                 |
      | "http://some.domain.com/p"                    | "a=1"          | "http://some.domain.com/p?a=1"                |
      | "http://some.domain.com"                      | "a=1,b=2"      | "http://some.domain.com?a=1&b=2"              |
      | "http://some.domain.com/"                     | "a=1,b=2"      | "http://some.domain.com/?a=1&b=2"             |
      | "http://some.domain.com/p"                    | "a=1,b=2"      | "http://some.domain.com/p?a=1&b=2"            |
      | "http://some.domain.com"                      | "c#=c++"       | "http://some.domain.com?c%23=c%2B%2B"         |
      | "http://some.domain.com"                      | "c#=c++,a a=@" | "http://some.domain.com?c%23=c%2B%2B&a+a=%40" |

