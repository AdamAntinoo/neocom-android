@NC02 @SplashActivity
Feature: [NC02]-Display the list of current registered and valid Credentials.

  Shows the list of registered credentials. Each credential represents a capsuleer account and allows to access the ESI date
  on behalf of that account.

  Additionaly adds a button to initiate the process to add more credentials to the list.

  @NC02.01
  Scenario: [NC02.01]-The Splash activity shows the custom action bar for the Splash.
	Given the AppSplashActivity
	When the AppSplashActivity activity lifecycle completes
	Then the AppSplashActivity has "1" pages
	And the activity has an action bar of type "SplashActionBar"
	And the SplashActionBar action bar contains the next fields
	  | fieldIdentifier     | fieldValue  |
	  | appName             | NEOCOM      |
	  | appVersion          | 0.17.1      |
	  | esiServer           | TRANQUILITY |
	  | esiServerState      | ONLINE      |
	  | capsuleersLabel     | CAPSULEERS  |
	  | capculeersConnected | 1234        |

  Scenario: [NC02.02]-The Splash activity shows the application info panel.
	Given the AppSplashActivity
	When the AppSplashActivity activity lifecycle completes
	Then the AppSplashActivity has "1" pages
	And the activity has "AppInfoPanel" on the header section
	And AppInfoPanel has the next data
	  | fieldIdentifier | fieldValue   |
	  | appName         | NEOCOM       |
	  | appVersion      | <appversion> |

  @NC02.03
  Scenario: [NC02.03]-The Splash activity shows the list of already registered credentials.
	Given the AppSplashActivity
	And the next Credentials registered
	  | accountId | accountName | accessToken | refreshToken | dataSource  | scope      |
	  | 123       | Tip Tophane | hghdg       | jkhjkshd===  | Tranquility | publicData |
	When the AppSplashActivity activity lifecycle completes
	Then the AppSplashActivity has "1" pages
	And the activity has "1" Credential on the data section
	And the Credential panel has the next rendered data
	  | fieldIdentifier | fieldValue    |
	  | name            | Tip Tophane   |
	  | characterId     | [#2113197470] |
	  | raceName        | CALDARI       |

  @NC02.04
  Scenario: [NC02.04]-The Splash activity has button on the data section to create more Credentials.
	Given the AppSplashActivity
	When the AppSplashActivity activity lifecycle completes
	Then the AppSplashActivity has "1" pages
	And the activity has a "NewLoginPanel" on the "Data" section
	And the NewLoginPanel panel has the next label
	  | fieldIdentifier | fieldValue                                          |
	  | label           | Click this button to add a new Credential (ESI/API) |

#  @NC02.05
#  Scenario: [NC02.05]-When a Credential is added to the model store for rendering the related jobs are created and registered on the Scheduler.
#	Given the AppSplashActivity
#	And the next Credentials registered
#	  | accountId | accountName | accessToken | refreshToken | dataSource  |
#	  | 211       | Tip Tophane | sdfer       | fgrt===      | Tranquility |
#	And a clear Scheduler
#	When the AppSplashActivity activity lifecycle completes
#	Then the Credential is added to the data sources
#	And the Credential adds the next jobs to the Scheduler
#	  | jobIdentifier          |
#	  | AssetDownloadProcessor |
