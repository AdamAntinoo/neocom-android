@PilotDashboardActivity
Feature: This activity shows the current selected pilot information such as the race information, the corporation and other basic
  information. Along with this data and icons there is a list of data areas like wallet or industry each of them represented by a
  panel with the are key information.

  @MVCPanel
  Scenario: [N04][PANEL] Mining data access information panel.
    Given the activity PilotDashboardActivity
    When the activity PilotDashboardActivity is started
    Then there is a data panel of type MiningData
    And that panel has an icon
    And that panel has a title with the literal "MINING DATA"
    And that panel has a label with the literal "Mining Resources market value"
    And that panel has a data field with contents "123.45M ISK"
    And that panel has an activator visible