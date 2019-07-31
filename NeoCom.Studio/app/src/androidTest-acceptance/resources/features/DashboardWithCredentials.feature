@SplashActivity
Feature: The dashboard will show the list of Credentials and the button to create new credentials.

  @MVCPanel
  Scenario: 01-Show the application name
    Given the activity "AppSplashActivity"
    When the activity is started
    Then we see a panel with the application name that matches "NeoCom"