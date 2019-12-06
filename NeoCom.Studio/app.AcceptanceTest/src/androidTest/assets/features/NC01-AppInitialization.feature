@NC01 @AppInitializationActivity
Feature: [NC01]-Acceptance for the application initialization process.

  The application requires some files and databases created and available at some point in time. During the initialization phase
  the application will check each one of the requirements and stop is any of them fails. The activity should then inform of the
  missing step and allow the user to restart again the initialization process.
  If the initialization process completes successfully then we move to the splash screen.

  Background:
	Given the AppInitializationActivity
	And the scheduler is disabled

  @NC01 @NC01.01
  Scenario: [NC01.01]-When the application starts with the network deactivated then the initialization phase should stop at the network
  check step.
	And the network is disabled
	When the AppInitializationActivity activity lifecycle completes
	Then there is a action bar of type AppVersion with the next fields
	  | AppName  | AppVersion |
	  | NeoComAT | v0.17.0     |
	And "12" panels on the LinearContainer
	And the LinearContainer has a "InitializationException" panel with the next information
	  | Message                                                            |
	  | The network is disabled. The application cannot continue properly. |
	And the LinearContainer has a "Retry" button with the next information
	  | Message                    |
	  | Restart the check process. |
