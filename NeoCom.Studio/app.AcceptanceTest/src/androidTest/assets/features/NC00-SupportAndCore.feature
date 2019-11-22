@NC00 @SettingsActivity
Feature: [NC00]-Do the tests for core and support functionalities that are to be used on the application or on testing.

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
