@NC01 @AppSplashActivity
Feature: [NC01] Setup and Open application databases.

  Check all the possible conditions to face during application startup and initialization. Check that the application data
  directories are on place and that the required databases are ready and with the latest version as configured on the
  application settings. If databases are obsolete run the upgrade scripts or copy the latest version from the
  application assets directory.

  @NC01.01
  Scenario: [NC01.01] Verify that the application initialization completes and that the component factory
	and other startup items are ready and running.
	Given the activity "AppSplashActivity"
	When selected the page "0" of the activity
	Then complete the lifecycle without exceptions
	And the component factory exists
	And the timer service is running
	And the processing spinner is visible

#  @NA01.01
#  Scenario: [NA01.01] When the application reaches the Splash page, be sure the page is rendered
#	Given the activity "AppSplashActivity"
#	When launching the activity to the onResume phase
#	Then complete the visualization setup and render the application version panel
#	And render the data panel with the onProgress spinner showing the elapsed time
#
  @NA01.02
  Scenario: [NA01.02] Once the Splash page has completed the initial render enter the onResume phase and start the process
  to check for the directories and files to be present to allow startup.
	Given the activity "AppSplashActivity" on the onResume phase
	And a new application private directory
	When entering the onResume phase
	Then check that the application private directory "exists"
	And check that the application cache directory "not exists"
	And check that the SDE database file on the application private directory "not exists"
#
#  @NA01.03
#  Scenario: [NA01.03] On this scenario we enter the onResume phase for the AppSplashActivity with a SDE database version
#	configured on the application and on the asset source. There is no SDE database file on the application private
#	directory so it is necessary to copy the SDE database from assets.
#	Given the activity AppSplashActivity on the onResume phase
#	And no SDE database file on the application private directory
#	And application SDE database version set to "20190530-0.16.0"
#	When entering the onResume phase
#	Then check that the SDE database file on the application private directory "not exists"
#	And copy the SDE database asset to the application private directory
#	And check that the SDE database file on the application private directory "exists"
#	And check that the SDE database version is "20190530-0.16.0"
#
#  @NA01.04
#  Scenario: [NA01.04] Enter the AppSplashActivity with a SDE database version not matching the application configured SDE
#	version.
#	Given the activity AppSplashActivity on the onResume phase
#	And SDE database file on the application private directory
#	And SDE database version set to "20190530-0.0.0"
#	And application SDE database version set to "20190530-0.16.0"
#	When entering the onResume phase
#	Then check that the SDE database file on the application private directory "exists"
#	And check that the SDE database version is "20190530-0.0.0"
#	And check that the SDE database version do not matches the application configured version
#	And copy the SDE database asset to the application private directory
#	And check that the SDE database version is "20190530-0.16.0"
