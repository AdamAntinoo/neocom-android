@NC06P0 @MiningLedgerActivity
Feature: [NC06P0] Acceptance actions related to the Mining Ledger features shown at the page 0 for daily extractions.

  Daily mining extractions will report a panel with the aggregated resources mined during the date of today and
  then separated panels for each mining location if there are more then one and section aggregated
  by hour with the resources mined during that period of time.

  Background:
	Given the MiningLegderActivity
	And the application storage is initialized
	And the scheduler is disabled
	And the following Credentials into my repository
	  | uniqueCredential     | accountId | accountName | dataSource  | accessToken                                                                                                                                                                      | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance   | assetsCount | raceName | miningValue |
	  | tranquility.92223647 | 92223647  | Beth Ripley | tranquility | CfDJ8HHFK/DOe6xKoNPHamc0mCW+m3KssaFdj/Gz5yz6LbeUZqk4wH1O/p+at7oiNS9OHwO+YY3wjMe+mXBCSsLKWMnbIf7qXeRyIb4hZZ1EAcrifvXRyD5V+V9NR8f2ti5LTx/QwAwo8g89dRmJyuHoDBFi0D0lpfxJOh9csWRWbozG | bQuMWmfQ8pDzPKaUtkYzFrlrdbajWLbroV6c49QSxPhli3OT2GLqoErgddXgwa2yTWsNEj7zOemFsmey-C4zZE-VV6tdlPHDEUS5w_aU8ckotYF1Ppc3DSdvRGeuVgxLM5CsZq1eNVlQIqyaZDj4aGHk7mRhjuYI8hzhct9Y9vATrF_DdYqvuxhw8RHtfQUc | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 2.76586637596E9 | 1290        | Minmatar | 0.0         |
	And the next "92223647" identifier for the CAPSULEER_IDENTIFIER
	And the following MiningExtractionEntity on backend response
	  | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |
	  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | <today>            | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | <today>            | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | <today>            | 11             | 92223647 |
	  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | <today>            | 10             | 92223647 |
	  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | <today>            | 10             | 92223647 |

  @NC06P0.01 @MiningLedgerActivity
  Scenario: [NC06P0.01][ACTIONBAR] Mining daily data action bar.
	When the MiningLegderActivity activity lifecycle completes for page "0"
	Then the MiningLegderActivity has "1" pages
#    And there is a action bar of type "MiningActionBar" with the next fields
#      | Title  | Subtitle |
#      | Beth Ripley | PLANET INSTALLATIONS - DETAILS   |
	And "6" panels on the "HeaderSection"
	And check a "DailyExtractionResourcesContainer" panel with the next contents
	  | nameTypeLabel           | quantityLabel | valueLabel |
	  | RESOURCE NAME [#TYPEID] | QUANTITY      | VALUE      |
	And check a "MiningResource" panel with the next contents
	  | resourceName   | resourceIdentifier | quantity | value          |
	  | Dense Veldspar | [#17471]           | 25,432   | 414,541.60 ISK |
#      | fieldIdentifier    | fieldValue      |
#      | resourceName       | Solid Pyroxeres |
#      | resourceIdentifier | [#17459]        |
#      | quantity           | 23.576          |
#      | value              | -23,576.00 ISK  |
#    And "5" panels on the "DataSection"
#    And the header section has a "AppInfoPanel" panel with the next information
#      | AppName  | AppVersion |
#      | NeoComAT | 0.19.4     |
	And stop on debug point



#    Given today date being "<today>"
#    And the activity "MiningLedgerActivity" with the next extras
#      | extraType | extraName            | extraValue |
#      | Integer   | CAPSULEER_IDENTIFIER | 92223647   |
#    And selected the page "0" of the activity
#    And the page variant set to "MINING_EXTRACTIONS_TODAY"
#    Then check that the custom action bar is present
#    And check a action bar "MiningActionBar" with the next contents
#      | fieldIdentifier | fieldValue                     |
#      | titleTopLeft    | Beth Ripley                    |
#      | subtitle        | PLANET INSTALLATIONS - DETAILS |

#  @skip_scenario @NC02P0.02 @MVCPanel
#  Scenario: [NC02P0.02][HEADER] Mining daily list of extraction resources.
#    Given today date being "<today>"
#    And the activity "MiningLedgerActivity" with the next extras
#      | extraType | extraName            | extraValue |
#      | Integer   | CAPSULEER_IDENTIFIER | 92223647   |
#    And selected the page "0" of the activity
#    And the page variant set to "MINING_EXTRACTIONS_TODAY"
#    When complete the lifecycle without exceptions
#    Then the header container has "4" panels
#    And check a "DailyExtractionResourcesContainer" panel with the next contents
#      | fieldIdentifier | fieldValue              |
#      | nameTypeLabel   | RESOURCE NAME [#TYPEID] |
#      | quantityLabel   | QUANTITY                |
#      | valueLabel      | VALUE                   |
#    And check a "MiningResource" panel with the next contents
#      | fieldIdentifier    | fieldValue      |
#      | resourceName       | Solid Pyroxeres |
#      | resourceIdentifier | [#17459]        |
#      | quantity           | 23.576          |
#      | value              | -23,576.00 ISK  |
#
#  @skip_scenario @NC02P0.03 @MVCPanel
#  Scenario: [NC02P0.02][DATA] Mining daily list of extractions by location and then by hour
#    Given today date being "<today>"
#    And the next records on the MiningRepository
#      | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |
#      | 2019-08-07:14-30001740-17464-92223647 | 17464  | 30001740      | 843      | 0     | <today>            | 14             | 92223647 |
#      | 2019-08-07:14-30001740-17460-92223647 | 17460  | 30001740      | 8266     | 0     | <today>            | 14             | 92223647 |
#      | 2019-08-07:14-30001740-17453-92223647 | 17453  | 30001740      | 3345     | 0     | <today>            | 14             | 92223647 |
#      | 2019-08-07:13-30001735-17459-92223647 | 17459  | 30001735      | 38087    | 14511 | <today>            | 13             | 92223647 |
#      | 2019-08-07:12-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | <today>            | 12             | 92223647 |
#      | 2019-08-07:12-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | <today>            | 12             | 92223647 |
#      | 2019-08-07:12-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | <today>            | 12             | 92223647 |
#      | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | <today>            | 11             | 92223647 |
#      | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | <today>            | 11             | 92223647 |
#      | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 1566     | 0     | <today>            | 10             | 92223647 |
#      | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 421      | 0     | <today>            | 10             | 92223647 |
#    And the number of records in "MiningRepository" is 11
#    And the activity "MiningLedgerActivity" with the next extras
#      | extraType | extraName            | extraValue |
#      | Integer   | CAPSULEER_IDENTIFIER | 92223647   |
#    And selected the page "0" of the activity
#    And the page variant set to "MINING_EXTRACTIONS_TODAY"
#    When complete the lifecycle without exceptions
#    Then the data container has "18" panels
#    And render a panel with "ResourceAggregatorContainer4EsiLocationFacetRender" and the next contents
#      | fieldIdentifier | fieldValue       |
#      | systemName      | Uhodoh           |
#      | extractionValue | 4,597,691.88 ISK |
#    And render a panel with "ResourceAggregatorContainer4LocalTimeFacetRender" and the next contents
#      | fieldIdentifier | fieldValue    |
#      | systemName      | 10            |
#      | extractionValue | 45,792,80 ISK |
