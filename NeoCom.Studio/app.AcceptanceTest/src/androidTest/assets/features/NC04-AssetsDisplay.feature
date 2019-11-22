@NC04 @AssetDisplayActivity
Feature: [NC04]-Define the pages to display the assets that belong to a pilot.

  Une of the most important things for a capsuleer is to know what is the inventory and where are those things. He/she
  needs to know where is any ot their ships or where are some materials ready for construction. Also important information
  if the current asset value and volume stored on each location to know it the can be moved wit the current ship.

  Among the other expected features is to allow to schedule logistic activities to assets are planned to be moved from one
  location to another because industry requirements or other contracting issues.

  The first asset display page shows the assets classified by their current storage location. Starting with the Region, then the
  station/structure and continuing to the station hangars and containers.

  @NC04.01
  Scenario: [NC04.01]-The pilot should be able to activate the AssetDisplayActivity from the Pilot dashboard.
	Given the PilotDashboardActivity
	When the pilot activates the Assets information panel
	Then the application moves to the AssetDisplayActivity.

  @NC04.02
  Scenario: [NC04.02]-The AssetDisplay activity has a single page. This page is the default and shows the title for assets classified by location.
  The page shows 2 Regions tht should be the paces where the Pilot has assets.
	Given the AssetDisplayActivity
	And the next list of assets persisted for character "92220000"
	  | is_singleton | item_id       | location_flag | location_id | location_type | quantity | type_id |
	  | true         | 1025460747649 | Hangar        | 60015001    | station       | 1        | 32880   |
	  | false        | 1025468308807 | Hangar        | 60015001    | station       | 1        | 1317    |
	  | true         | 1026745455214 | AssetSafety   | 60000475    | station       | 1        | 22542   |
	  | true         | 1031786258314 | Hangar        | 60014806    | station       | 1        | 601     |
	When the AssetDisplayActivity activity lifecycle completes
	Then there is a action bar with the next fields
	  | fieldIdentifier   | fieldValue         |
	  | toolBarTitleLeft  | Perico Tuerto      |
	  | toolBarSubTitle   | ASSETS BY LOCATION |
	  | toolBarTitleRight | [#93813310]        |
	And "0" panels on the "HeaderSection"
	And "2" panels on the "DataSection"
	And a type Region panel on the "DataSection" with the next data
	  | regionId | regionName | stationCount |
	  | 10000016 | Metropolis | 1            |
	And a type "Region" panel on the "DataSection" with expansion enabled

  @NC04.03
  Scenario: [NC04.03]-The region panels should have the right arrow to allow expansion to show the related stations/structures.
	Given the AssetDisplayActivity
	And the next list of assets persisted for character "92220000"
	  | is_singleton | item_id       | location_flag | location_id | location_type | quantity | type_id |
	  | true         | 1025460747649 | Hangar        | 60015001    | station       | 1        | 32880   |
	  | false        | 1025468308807 | Hangar        | 60015001    | station       | 1        | 1317    |
	  | true         | 1026745455214 | AssetSafety   | 60000475    | station       | 1        | 22542   |
	  | true         | 1031786258314 | Hangar        | 60014806    | station       | 1        | 601     |
	When the AssetDisplayActivity activity lifecycle completes
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
