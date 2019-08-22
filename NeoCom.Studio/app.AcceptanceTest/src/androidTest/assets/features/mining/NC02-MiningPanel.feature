@NC02P0 @MiningLedgerActivity
Feature: [NC02P0] Acceptance actions related to the Mining Ledger features shown at the page 0 for daily extractions.

  Daily mining extractions will report a panel with the aggregated resources mined during the date of today and
  then separated panels for each mining location if there are more then one and section aggregated
  by hour with the resources mined during that period of time.

  Background:
	Given an initialized application
	And the application access privileges
	And create the application public directory
	And create the application cache directory
	And copy the SDE database asset to the application private directory
	And the reconfigured neocom component factory
	And check that the SDE database file on the application private directory "exists"
#	And check that the SDE database version is "20190530-0.16.0"
	And a clean application repository
	And the next records on the CredentialRepository
	  | uniqueCredential     | accountId | accountName   | dataSource  | accessToken                                                                                                                                                                      | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance   | assetsCount | raceName | miningResourcesEstimatedValue |
	  | tranquility/93813310 | 93813310  | Perico Tuerto | tranquility | CfDJ8HHFK/DOe6xKoNPHamc0mCXhPScKeLZLosJ4uMphVlAFxrs/gfhbfOFSrZAb+/8ppOOJdta9jvrkDrihXHfAKeef3jntpGDor+iUnTsAaBEOOT6x58gq59jF46W6iSuj2AxFNE8iHs1z65ePOQYEqyQBGWikEvDlb1zQ8PRWfqdN | O5YPDzUXyJW4S8lCr-lu49YFwSZaE4_GCN59bu1kF2x6fqD_-lYy-n1x7o87D_OctkIMpvzQ2x2whHzXMKKBB1FAzYesVPKfc0lT4s9csGF6aRRBanBhsMZsOEdrPw5kdBRbiOzEwg5EH8IxyYoyCsTY6KnJvyyIt84ee_Svfuc1CM8U3igKGsw7f31xUd5I | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 6.309543632E8   | 1476        | Amarr    | 0.0                           |
	  | tranquility/92002067 | 92002067  | Adam Antinoo  | tranquility | CfDJ8HHFK/DOe6xKoNPHamc0mCVQkXTQ7dUcUp0kHOg5umD/vTdhxew97SH0Wu4iGGzHiHLYXww1EJir+5F37oxCBXm+tQsoNvSE3oYjqbJAjcFBeWXiqbJ4P4U3iMAB0bS0zd9oCyy5JFmfO31PTjYcP/Ffr5AruC9/mTnp4ET5ZlXD | uQBTlJuTANhCxK__-ED27C-wgGszdjG1UUuyR8vKY807ck7isEanHGAB0CRexGwSgJp5M9OwQt6n_kVeEOzxI0imhJiocbfDWThlWtbeZy_TfaOKruxCfz8rrkWw9S5YGOcJvh8u53rEhPCB66OxNKYfqXIAJBuWIo1vukiUWRD1UyV3A_QV64UiqIEzKcB1 | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 2.27058387661E9 | 998         | Minmatar | 0.0                           |
	  | tranquility/92223647 | 92223647  | Beth Ripley   | tranquility | CfDJ8HHFK/DOe6xKoNPHamc0mCW+m3KssaFdj/Gz5yz6LbeUZqk4wH1O/p+at7oiNS9OHwO+YY3wjMe+mXBCSsLKWMnbIf7qXeRyIb4hZZ1EAcrifvXRyD5V+V9NR8f2ti5LTx/QwAwo8g89dRmJyuHoDBFi0D0lpfxJOh9csWRWbozG | bQuMWmfQ8pDzPKaUtkYzFrlrdbajWLbroV6c49QSxPhli3OT2GLqoErgddXgwa2yTWsNEj7zOemFsmey-C4zZE-VV6tdlPHDEUS5w_aU8ckotYF1Ppc3DSdvRGeuVgxLM5CsZq1eNVlQIqyaZDj4aGHk7mRhjuYI8hzhct9Y9vATrF_DdYqvuxhw8RHtfQUc | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 2.76586637596E9 | 1290        | Minmatar | 0.0                           |
	And the number of records in "CredentialRepository" is 3
	And the next records on the MiningRepository
	  | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |
	  | 2019-08-08:24-30001735-17459-92223647 | 17459  | 30001735      | 14511    | 0     | 2019-08-08         | 24             | 92223647 |
	  | 2019-08-08:12-30001735-17459-92223647 | 17459  | 30001735      | 14511    | 0     | 2019-08-08         | 12             | 92223647 |
	  | 2019-08-07:24-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 0     | 2019-08-07         | 24             | 92223647 |
	  | 2019-08-07:24-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | 2019-08-07         | 24             | 92223647 |
	  | 2019-08-07:24-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 0     | 2019-08-07         | 24             | 92223647 |
	  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | 2019-08-07         | 10             | 92223647 |
	  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | 2019-08-07         | 10             | 92223647 |
	And the number of records in "MiningRepository" is 10
	And the activity "MiningLedgerActivity" with the next extras
	  | extraType | extraName            | extraValue |
	  | Integer   | CAPSULEER_IDENTIFIER | 92223647   |
	And selected the page "0" of the activity
	And the page variant set to "MINING_EXTRACTIONS_TODAY"

  @NC02P0.01 @MVCPanel
  Scenario: [NC02P0.01][ACTIONBAR] Mining daily data action bar.
	Given today date being "2019-08-08"
#	When complete the lifecycle without exceptions
	Then the action bar for this page shows the next fields
	  | Title  | pilotName    | pageSubTitle |
	  | MINING | Adam Antinoo | MINING DAILY |
#
#  @NC02P0.02 @MVCPanel
#  Scenario: [NC02P0.01][HEADER] Mining daily list of extraction resources.
#	Given today date being "2019-08-07"
#	When complete the lifecycle without exceptions
#	Then the header container has the next list of panels
#	  | panelClass | typeId | quantity | value  |
#	  | Resource   | 17459  | 23576    | 235760 |
#	  | Resource   | 17464  | 30348    | 303480 |
#	  | Resource   | 17471  | 25432    | 254320 |
