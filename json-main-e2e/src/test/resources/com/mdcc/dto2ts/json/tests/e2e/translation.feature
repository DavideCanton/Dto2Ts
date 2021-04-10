Feature: Translation of DTO

  Scenario: Translation of single DTO with simple fields
    Given the schema "simple"
    And the DTO ExampleDto

    When I invoke the program to translate it

    Then a file named "example-dto.model.ts" should have been created
    And imports in "example-dto.model.ts" should be:
      | importedSymbols                      | from              |
      | JsonClass, JsonProperty, SerializeFn | at-json           |
      | JsonDateISO                          | creapp-common-lib |
