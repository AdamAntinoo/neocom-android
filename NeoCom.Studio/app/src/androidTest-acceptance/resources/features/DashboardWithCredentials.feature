@SplashActivity
Feature: The dashboard will show the list of Credentials and the button to create new credentials.

  @MVCPanel
  Scenario: 01-Show the application name
    Given the activity AppSplashActivity
    When the activity is started
    Then we have an application panel
    And that panel has an ApplicationName field
    And that field shows the application name that matches "NeoCom"
