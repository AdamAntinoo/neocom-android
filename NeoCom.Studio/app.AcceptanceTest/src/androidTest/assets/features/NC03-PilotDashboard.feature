@NC03 @PilotDashboardActivity
Feature: [NC03]-Shows a page with all the activities and managers available for the pilot along with some key summary information.

  Shows summary information about the current selected pilot and it's corporation. Has buttons to access the different managers
  and features available to the pilot.

  @NC03.01
  Scenario: [NC03.01]-The pilot dashboard shows a header with corporation and summary data.
	Given the PilotDashboardActivity
	And the following Credentials into my repository
	  | uniqueCredential       | accountId  | accountName | dataSource  | accessToken                                                                                                                                                                         | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance | assetsCount | raceName |
	  | tranquility/2113197470 | 2113197470 | Tip Tophane | tranquility | 1\|CfDJ8O+5Z0aH+aBNj61BXVSPWfgfA+LRLuqiQb+vN2qU9iLe7VrEPWBzaJbci7NDjHi/DIyYhefboighuR5ibHUkwFaYuFn6iDjVweDpBzLnl1KOSUX1Ek3dkJfisU/gO2+CHKlL1z8wN0h3Cm44rZsG1FS/9LK42bQaNUtpmFWYu705 | F3RxYALkWzgsRwU2o_n-q3LZ6Mv4WBHKT0LzLnKy7IBD89d7y7cxgmAJX07kUa7YmhkvqGdYJUjhT7fOJz7syh1nLY1mrif4e-zp0xEL0JZpJciRN3rdMabXdgtvbgjn8SoQpb4Q0i2kvshBJL7c4hE3PGE21ZgfDfXr4_bW0uFyfuTYZx1JrjwnwKUP7pKS | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 240134.82     | 40          | Caldari  |
	When the PilotDashboardActivity activity lifecycle completes for page "0"
	Then there is a action bar of type "DashboardActionBar" with the next fields
	  | AppName  | AppVersion | PilotName   |
	  | NeoComAT | 0.17.0     | Tip Tophane |
	Then the PilotDashboardActivity has "1" pages
	And "1" panels on the "HeaderSection"
	And "4" panels on the "DataSection"
	And the header has a "Pilot" panel with the next information
	  | PilotName   | PilotId       | PilotRace | CorporationName             |
	  | Tip Tophane | [#2113197470] | CALDARI   | School of Applied Knowledge |
