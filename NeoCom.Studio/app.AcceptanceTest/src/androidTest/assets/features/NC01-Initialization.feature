@NC01 @AppInitializationActivity
Feature: [NC01]-Acceptance for the application initialization process.

  The application requires some files and databases created and available at some point in time. During the initialization phase
  the application will check each one of the requirements and stop is any of them fails. The activity should then inform of the
  missing step and allow the user to restart again the initialization process.
  If the initialization process completes successfully then we move to the splash screen.

  Scenario: [NC01.01]-Check that the activity open and completes the Creation phase with an operative LinearContainer.
	Given the AppInitializationActiviy activity
	When completed the onCreate lifecycle phase
	Then we can access a LinearContainer empty of content