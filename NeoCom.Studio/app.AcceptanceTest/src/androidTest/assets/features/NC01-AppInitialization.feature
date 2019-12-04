@NC01 @AppInitializationActivity
Feature: [NC01]-Acceptance for the application initialization process.

  The application requires some files and databases created and available at some point in time. During the initialization phase
  the application will check each one of the requirements and stop is any of them fails. The activity should then inform of the
  missing step and allow the user to restart again the initialization process.
  If the initialization process completes successfully then we move to the splash screen.

  @skip_scenario @NC01 @NC01.01
  Scenario: [NC01.01]-When the application starts with the network deactivated then the initialization phase should stop at the network
  check step.
	Given the AppInitializationActivity
	And the network is disabled
	When the AppSplashActivity activity lifecycle completes for page "0"
	Then there is a action bar of type NeoComActionBar with the next fields
	  | AppName  | AppVersion |
	  | NeoComAT | 0.17.0     |
	And "1" panels on the "HeaderSection"
	And "10" panels on the "DataSection"
	And the last panel on the data section is of type Exception
	And the data section has a "InitializationException" panel with the next information
	  | Message                                                          |
	  | The network is disabled so the application cannot work properly. |

#  @NC0x	@NC01.02
#  Scenario: [NC01.02]-Check that the activity open and completes the onStart phase with an operative LinearContainer.
#	Given the AppInitializationActiviy activity
#	When completed the onStart lifecycle phase
#	Then we have layout with a single item
#	And that item is the application info panel
#	And that the application version matches "0.17.0"
