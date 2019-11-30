@NC00 @SettingsActivity
Feature: [NC00]-Do the tests for core and support functionality that are to be used on the application or on testing.

  There are new development and key elements for acceptance tests that I think require their own testing. This scenarios
  will deal with such tests that should be run under the acceptance test configuration.

  @NC00.01
  Scenario: [NC00.01]-Validate the exact order flow for the Ristretto wait4Completion method.
	Given the SettingsActivity
	When performing the UI thread test
	Then we get the right order of results

  @NC00.02
  Scenario: [NC00.02]-Validate the exact order flow for the Ristretto waitForBackground method.
	Given the SettingsActivity
	When performing the background synchronization test
	Then we get the right order of results

  @NC00.03
  Scenario: [NC00.03]-Generate an exception when on the Activity lifecycle to check the rendering for exceptions.
	Given the AppInializationActivity
	When the activity enters the OnCreate stage there is an exception
	Then get the exception panel visible with the next data
	  |

  @NC00.04
  Scenario: [NC00.04]-Check that the application initialization order creates the expected instances and services.
	Given an initialized application
	And the application access privileges
	Then there is a Minute service running
	And a component factory available
	And the required privileges to use private storage
	And the required privileges to use public storage
