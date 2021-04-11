Feature: Translation of DTO

  Scenario Outline: Translation of single DTO with simple fields
    Given the test case "<testCase>"
    And the DTO <dtoName>

    When I invoke the program to translate it

    Then files generated should be the ones in output folder
    And content of each file should match

    Examples:
      | testCase | dtoName    |
      | test01   | ExampleDto |