@AppSplashActivity
Feature: The dashboard will show the list of Credentials and the button to create new credentials.

  @MVCPanel
  Scenario: 01-Show the application name
    Given the activity AppSplashActivity
    When the activity is started
    Then we have an application panel
    And that panel has an ApplicationName field
    And that field shows the application name that matches "NeoCom"

  Scenario: 01-Show a header panel with the application name and the application version.
    Given the activity AppSplashActivity
    When the activity is started
    Then there is a header panel
    And that panel has an ApplicationName field with value "NeoCom"
    And that panel has an ApplicationVersion field with value "v0.16.0"

  Scenario: 02-Show a data panel with the literal 'CREDENTIAL LIST' and used as a separator to
  the list of Credentials.
    Given the activity AppSplashActivity
    When the activity is started
    Then there is a data panel of type PanelTitle
    And that panel shows the title "CREDENTIAL LIST"

  Scenario: 03-Show a data panel with the Credential data. This item replicates for each credential instance found on the repository.
    Given the activity AppSplashActivity
    When the activity is started
    Then there is a data panel of type Credential
    And that panel contains "Perico Tuerto" on the field pilotName
    And that panel contains "[#93813310]" on the field pilotIdentifier