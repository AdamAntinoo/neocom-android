@NC02 @SplashActivity
Feature: [NC02]-Display the list of current registered and valid Credentials.

  Shows the list of registered credentials. Each credential represents a capsuleer account and allows to access the ESI date
  on behalf of that account.

  Additionally adds a button to initiate the process to add more credentials to the list.

  @skip_scenario @NC02.01
  Scenario: [NC02.01]-The Splash activity shows the custom action bar for the Splash along with the application version panel on the
  header and the list of credentials on the data section.
	Given the AppSplashActivity
	And the following Credentials into my repository
	  | uniqueCredential       | accountId  | accountName | dataSource  | accessToken                                                                                                                                                                         | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance | assetsCount | raceName |
	  | tranquility/2113197470 | 2113197470 | Tip Tophane | tranquility | 1\|CfDJ8O+5Z0aH+aBNj61BXVSPWfgfA+LRLuqiQb+vN2qU9iLe7VrEPWBzaJbci7NDjHi/DIyYhefboighuR5ibHUkwFaYuFn6iDjVweDpBzLnl1KOSUX1Ek3dkJfisU/gO2+CHKlL1z8wN0h3Cm44rZsG1FS/9LK42bQaNUtpmFWYu705 | F3RxYALkWzgsRwU2o_n-q3LZ6Mv4WBHKT0LzLnKy7IBD89d7y7cxgmAJX07kUa7YmhkvqGdYJUjhT7fOJz7syh1nLY1mrif4e-zp0xEL0JZpJciRN3rdMabXdgtvbgjn8SoQpb4Q0i2kvshBJL7c4hE3PGE21ZgfDfXr4_bW0uFyfuTYZx1JrjwnwKUP7pKS | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 240134.82     | 40          | Caldari  |
	When the AppSplashActivity activity lifecycle completes for page "0"
	Then the AppSplashActivity has "1" pages
	And there is a action bar of type SplashActionBar with the next fields
	  | AppName  | AppVersion | ServerName  | ServerState | PilotCount |
	  | NeoComAT | 0.17.0     | Tranquility | ONLINE      | 1.234      |
	And "1" panels on the "HeaderSection"
	And "4" panels on the "DataSection"
	And the header section has a "AppInfoPanel" panel with the next information
	  | AppName  | AppVersion |
	  | NeoComAT | 0.17.0     |
	And the data section has a "Credential" panel with the next information
	  | PilotName   | PilotId       | PilotRace | PilotBalance   | PilotAssetCount |
	  | Tip Tophane | [#2113197470] | CALDARI   | 240,134.82 ISK | 40              |

  @skip_scenario @NC02.02
  Scenario: [NC02.02]-The Splash activity has a button on the data section to create more Credentials and this button is separated from the
  list of credentials by a white separator.
	Given the AppSplashActivity
	And the following Credentials into my repository
	  | uniqueCredential       | accountId  | accountName | dataSource  | accessToken                                                                                                                                                                         | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance | assetsCount | raceName |
	  | tranquility/2113197470 | 2113197470 | Tip Tophane | tranquility | 1\|CfDJ8O+5Z0aH+aBNj61BXVSPWfgfA+LRLuqiQb+vN2qU9iLe7VrEPWBzaJbci7NDjHi/DIyYhefboighuR5ibHUkwFaYuFn6iDjVweDpBzLnl1KOSUX1Ek3dkJfisU/gO2+CHKlL1z8wN0h3Cm44rZsG1FS/9LK42bQaNUtpmFWYu705 | F3RxYALkWzgsRwU2o_n-q3LZ6Mv4WBHKT0LzLnKy7IBD89d7y7cxgmAJX07kUa7YmhkvqGdYJUjhT7fOJz7syh1nLY1mrif4e-zp0xEL0JZpJciRN3rdMabXdgtvbgjn8SoQpb4Q0i2kvshBJL7c4hE3PGE21ZgfDfXr4_bW0uFyfuTYZx1JrjwnwKUP7pKS | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 240134.82     | 40          | Caldari  |
	When the AppSplashActivity activity lifecycle completes for page "0"
	Then the AppSplashActivity has "1" pages
	And the data section has a Separator panel of "white" color
	And the data section has a "NewLoginPanel" panel with the next information
	  | Buttonlabel                                         |
	  | Click this button to add a new Credential (ESI/API) |

  @skip_scenario @NC02.03
  Scenario: [NC02.03]-When the credential list is processed and the list of models is generated then we also register the corresponding
  background scheduler tasks.
	Given the AppSplashActivity
	And the following Credentials into my repository
	  | uniqueCredential       | accountId  | accountName | dataSource  | accessToken                                                                                                                                                                         | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance | assetsCount | raceName |
	  | tranquility/2113197470 | 2113197470 | Tip Tophane | tranquility | 1\|CfDJ8O+5Z0aH+aBNj61BXVSPWfgfA+LRLuqiQb+vN2qU9iLe7VrEPWBzaJbci7NDjHi/DIyYhefboighuR5ibHUkwFaYuFn6iDjVweDpBzLnl1KOSUX1Ek3dkJfisU/gO2+CHKlL1z8wN0h3Cm44rZsG1FS/9LK42bQaNUtpmFWYu705 | F3RxYALkWzgsRwU2o_n-q3LZ6Mv4WBHKT0LzLnKy7IBD89d7y7cxgmAJX07kUa7YmhkvqGdYJUjhT7fOJz7syh1nLY1mrif4e-zp0xEL0JZpJciRN3rdMabXdgtvbgjn8SoQpb4Q0i2kvshBJL7c4hE3PGE21ZgfDfXr4_bW0uFyfuTYZx1JrjwnwKUP7pKS | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 240134.82     | 40          | Caldari  |
	When the AppSplashActivity activity lifecycle completes for page "0"
	Then the list of registered scheduled tasks increases in "1"


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
