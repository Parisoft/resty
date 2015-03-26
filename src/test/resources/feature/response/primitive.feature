#language:en
Feature: Primitive entity conversion

  Scenario Outline: Convert entity to primitive type
    When do a GET request to primitive with value <expectedValue>
    Then the response entity is converted to a <expectedClassName> with value <expectedValue>

    Examples: 
      | expectedValue   | expectedClassName     |
      | "I'm a string!" | "java.lang.String"    |
      | "08"            | "java.lang.Byte"      |
      | "426"           | "java.lang.Short"     |
      | "426"           | "java.lang.Integer"   |
      | "426"           | "java.lang.Long"      |
      | "4.04"          | "java.lang.Float"     |
      | "4.04"          | "java.lang.Double"    |
      | "TruE"          | "java.lang.Boolean"   |
      | "fAlSe"         | "java.lang.Boolean"   |
