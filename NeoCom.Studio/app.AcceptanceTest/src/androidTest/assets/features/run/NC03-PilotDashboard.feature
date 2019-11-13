@NC03 @PilotDashboardActivity
Feature: [NC03]-Shows a page with all the activities and managers available for the pilot along with some key summary information.

  Shows summary information about the current selected pilot and it's corporation. Has buttons to access the different managers
  and features available to the pilot.

  Scenario: [NC03.01]-The pilot dashboard shows a header with corporation and summary data.
	Given the PilotDashboardActivity
	When the PilotDashboardActivity activity lifecycle completes
	Then the PilotDashboardActivity has "1" pages
	And the activity has an action bar with title "Pilot Data"
	And the header has a "PilotData" panel with the next information
	  | fieldIdentifier | fieldValue                                                              |
	  | panelLabel      | There are no assets for this pilot. Wait for asset download completion. |
