@NC04 @AssetDisplayActivity
Feature: [NC04]-Define the pages to display the assets that belong to a pilot.

  One of the most important things for a capsuleer is to know what is the inventory and where are those things. He/she
  needs to know where is any ot their ships or where are some materials ready for construction. Also important information
  is the current asset value and volume stored on each location to know it the can be moved with the current ship.

  Among the other expected features is to allow to schedule logistic activities to assets are planned to be moved from one
  location to another because industry requirements or other contracting issues.

  The first asset display page shows the assets classified by their current storage location. Starting with the Region, then the
  Station/Structure and continuing to the station Hangars and Containers.

  Background:
	Given the AssetDisplayActivity
	And the following Credentials into my repository
	  | uniqueCredential       | accountId  | accountName | dataSource  | accessToken                                                                                                                                                                         | refreshToken                                                                                                                                                                                     | scope                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | walletBalance | assetsCount | raceName |
	  | tranquility/2113197470 | 2113197470 | Tip Tophane | tranquility | 1\|CfDJ8O+5Z0aH+aBNj61BXVSPWfgfA+LRLuqiQb+vN2qU9iLe7VrEPWBzaJbci7NDjHi/DIyYhefboighuR5ibHUkwFaYuFn6iDjVweDpBzLnl1KOSUX1Ek3dkJfisU/gO2+CHKlL1z8wN0h3Cm44rZsG1FS/9LK42bQaNUtpmFWYu705 | F3RxYALkWzgsRwU2o_n-q3LZ6Mv4WBHKT0LzLnKy7IBD89d7y7cxgmAJX07kUa7YmhkvqGdYJUjhT7fOJz7syh1nLY1mrif4e-zp0xEL0JZpJciRN3rdMabXdgtvbgjn8SoQpb4Q0i2kvshBJL7c4hE3PGE21ZgfDfXr4_bW0uFyfuTYZx1JrjwnwKUP7pKS | publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 | 240134.82     | 40          | Caldari  |
	And the next "2113197470" identifier for the CAPSULEER_IDENTIFIER

  @skip_scenario @NC04.01
  Scenario: [NC04.01]-Show the assets display pages when there are no assets. The page should show a button to request a refresh of
  the pilot assets from the ESI server.
	And an empty list of assets
	When the AssetDisplayActivity activity lifecycle completes for page "0"
	Then a type NoAssetsInformation panel on the "DataSection" with the next data
	  | Title                                                                  |
	  | No assets ready for current pilot. Wait for asset download completion. |

  @NC04.02
  Scenario: [NC04.02]-The AssetDisplay activity has a single page. This page is the default and shows the title for assets classified by location.
  The page shows 2 Regions that should be the paces where the Pilot has assets.
	And the next list of assets persisted for character "2113197470"
	  | is_singleton | item_id       | location_flag | location_id | location_type | quantity | type_id |
	  | true         | 1025460747649 | Hangar        | 60015001    | station       | 1        | 32880   |
	  | false        | 1025468308807 | Hangar        | 60015001    | station       | 1        | 1317    |
	  | true         | 1026745455214 | AssetSafety   | 60000475    | station       | 1        | 22542   |
	  | true         | 1031786258314 | Hangar        | 60014806    | station       | 1        | 601     |
	When the AssetDisplayActivity activity lifecycle completes for page "0"
	Then there is a action bar with the next fields
	  | PilotName   | PilotIdentifier | PageName           |
	  | Tip Tophane | [#2113197470]   | ASSETS BY LOCATION |
	And "0" panels on the "HeaderSection"
	And "2" panels on the "DataSection"
	And a type Region panel on the "DataSection" with the next data
	  | regionId | regionName | stationCount |
	  | 10000016 | Metropolis | 1            |
	And a type "Region" panel on the "DataSection" with expansion enabled

  @NC04.03
  Scenario: [NC04.03]-The region panels should have the right arrow to allow expansion to show the related stations/structures.
	And the next list of assets persisted for character "2113197470"
	  | is_singleton | item_id       | location_flag | location_id | location_type | quantity | type_id |
	  | true         | 1025460747649 | Hangar        | 60015001    | station       | 1        | 32880   |
	  | false        | 1025468308807 | Hangar        | 60015001    | station       | 1        | 1317    |
	  | true         | 1026745455214 | AssetSafety   | 60000475    | station       | 1        | 22542   |
	  | true         | 1031786258314 | Hangar        | 60014806    | station       | 1        | 601     |
	When the AssetDisplayActivity activity lifecycle completes for page "0"
	And when the region is expanded then there are "3" panels on the "DataSection"
	And a type "Station" panel on the "DataSection" with expansion enabled
	And a type Station panel on the "DataSection" with the next data
	  | regionName | systemName | stationName                      | stationIdentifier | contentsCount |
	  | Metropolis | Nakugard   | Nakugard V - Republic University | [40132049]        | 1             |

#
#  Scenario: [NC04.03]-The AssetDisplayActivity.P0 has an action bar with asset specific information.
#	Given the AssetDisplayActivity Page0
#	When the page is selected
#	Then the action bar shows the pilot avatar icon
#	And the action bar shows the pilot's name
#	And the action bar shows the title "Assets By Location"
#
#  Scenario: [NC04.04]-The AssetDisplayActivity.P0 has not head contents. On the data contents there is a list of Regions.
#
#  Scenario: [NC04.04]-Each Region contains the region data along a summary of the number of locations and assets in the region.
