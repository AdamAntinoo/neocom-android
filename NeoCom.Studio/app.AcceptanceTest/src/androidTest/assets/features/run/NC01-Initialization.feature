@NC01 @AppInitializationActivity
Feature: [NC01]-Acceptance for the application initialization process.

  The application requires some files and databases created and available at some point in time. During the initialization phase
  the application will check each one of the requirements and stop is any of them fails. The activity should then inform of the
  missing step and allow the user to restart again the initialization process.
  If the initialization process completes successfully then we move to the splash screen.

  @NC01 @NC01.01
  Scenario: [NC01.01]-Check that the activity open and completes the onCreate phase with an empty page.
	Given the AppInitializationActiviy activity
	When completed the onCreate lifecycle phase
	Then we have an empty view layout

  @NC01	@NC01.02
  Scenario: [NC01.02]-Check that the activity open and completes the onStart phase with an operative LinearContainer.
	Given the AppInitializationActiviy activity
	When completed the onStart lifecycle phase
	Then we have layout with a single item
	And that item is the application info panel
	And that the application version matches "0.17.0"