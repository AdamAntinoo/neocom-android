@NC02P0 @MiningLedgerActivity
Feature: [NC02P0] Acceptance actions related to the Mining Ledger features shown at the page 0 for daily extractions.

  Daily mining extractions will report a panel with the aggregated resources mined during the date of today and
  then separated panels for each mining location if there are more then one and section aggregated
  by hour with the resources mined during that period of time.

  Background:
	Given the reconfigured neocom component factory
	And a new application repository
	And the next records on the CredentialRepository
	  | uniqueCredential     | accountId | accountName  | dataSource  | accessToken                                                                            | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance | assetsCount | miningResourcesEstimatedValue |
	  | tranquility/92002067 | 92002067  | Adam Antinoo | tranquility | lfS7LIBbjLKnglJsujkNERgbwgOE0dCDiudhCdyrBxbxRp1xtFYzTRMxY2G2EssiS44UvvdMfRrXiLtn0SW9Zw | oCHpz8dm7MJNZ6PYqRvpWU6IkaD_Z5PsNx9SkI54UkvBY92yIUqEpIiFv03nxnLLnx-w_uTBBmsITYxM7WqzjUio4pXTJJN-GUGb-YNBfe0YNia_fl-NUNlmIGCwIMQCQhLpDZEUmECKUt7Do4T9ZW7FimJrhJUyw5xumUPN-d64oeY7Nd-UO4mc-By8i3aQ | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 0.0           | 0           | 0.0                           |
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
	And the activity "MiningLedgerActivity" with the next extras
	  | extraName       | extraValue |
	  | pilotCredential | 92002067   |
	And selected the page "0" of the activity
	And the page variant set to "TODAY MINING EXTRACTIONS"

  @NC02P0.01 @MVCPanel
  Scenario: [NC02P0.01][ACTIONBAR] Mining daily data action bar.
	Given today date being "2019-08-08"
	When complete the lifecycle without exceptions
	Then the action bar for this page shows the next fields
	  | Title  | pilotName    | pageSubTitle |
	  | MINING | Adam Antinoo | MINING DAILY |

  @NC02P0.02 @MVCPanel
  Scenario: [NC02P0.01][HEADER] Mining daily list of extraction resources.
	Given today date being "2019-08-07"
	When complete the lifecycle without exceptions
	Then the header container has the next list of panels
	  | panelClass | typeId | quantity | value  |
	  | Resource   | 17459  | 23576    | 235760 |
	  | Resource   | 17464  | 30348    | 303480 |
	  | Resource   | 17471  | 25432    | 254320 |
