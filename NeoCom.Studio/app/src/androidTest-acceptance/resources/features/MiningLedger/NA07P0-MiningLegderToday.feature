@NA07 @NA07.P0 @MiningLedgerActivity
Feature: [NA07][P0] Mining Ledger Daily mining extractions.

  This page shows the mining extractions performed during the current date. The page has the common core sections.
  The action bar for the Mining Ledger Daily will show the select pilot's name and the page label
  The page header will have a table header with the description of the column data and a list of the aggregated
  mining resources extracted during the date.
  The page data will contain renders for the different systems where the extractions have been done and for each
  system the hourly extracted data if there are extractions that have continuity over more that one hour.

  @NA07.P0.HEADER.01
  Scenario: [NA07.P0.HEADER.01] The header contains a table header with the labels that describe the header table data.
	Given a "MiningLedgerActivity" active at page "MINING_EXTRACTIONS_TODAY"
	When the activity completes data processing and render
	Then there is a "ResourceHeader" panel on the "Header" render component
	And the panel has theme set to "WHITE"
	And showing the next panel fields and contents
	  | resourceNameLabel       | quantityLabel | valueLabel |
	  | RESOURCE NAME [#TYPEID] | QUANTITY      | VALUE      |

  @NA07.P0.HEADER.02
  Scenario: [NA07.P0.HEADER.02] The header contains a list of extracted resources with a 3 column set of data.
	Given a "MiningLedgerActivity" active at page "MINING_EXTRACTIONS_TODAY"
	And an empty Mining Extraction repository
	And the next records on the MiningRepository
	  | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |
	  | 2019-08-07:13-30001735-17459-92223647 | 17459  | 30001735      | 38087    | 14511 | 2019-08-07         | 13             | 92223647 |
	  | 2019-08-07:12-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:12-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:12-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 1566     | 0     | 2019-08-07         | 10             | 92223647 |
	  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 421      | 0     | 2019-08-07         | 10             | 92223647 |
	And extractions data updated to current date
	When the activity completes data processing and render
	Then there are "3" "Resource" panels on the "Header" render component
	And the panel has theme set to "GREEN"
	And showing the next panel fields and contents
	  | resourceName    | resourceId | quantity | value          |
	  | Solid Pyroxeres | [#17459]   | 38087    | -38,087.00 ISK |
	  | Solid Pyroxeres | [#17471]   | 25432    | -25,432.00 ISK |
	  | Solid Pyroxeres | [#17464]   | 30348    | -30,348.00 ISK |

  @NA07.P0.DATA.03
  Scenario: [NA07.P0.DATA.03] The data section contains a single location extraction report for today extractions with
  mining extractions performed during 3 hours and showing a set of records grouped under each of the extraction hours.
	Given a "MiningLedgerActivity" active at page "MINING_EXTRACTIONS_TODAY"
	And an empty Mining Extraction repository
	And the next records on the MiningRepository
	  | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |
	  | 2019-08-07:13-30001735-17459-92223647 | 17459  | 30001735      | 38087    | 14511 | 2019-08-07         | 13             | 92223647 |
	  | 2019-08-07:12-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:12-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:12-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 1566     | 0     | 2019-08-07         | 10             | 92223647 |
	  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 421      | 0     | 2019-08-07         | 10             | 92223647 |
	When the activity completes data processing and render
	Then there are "1" "ResourceGroup" panels on the "Data" render component
	And the panel has theme set to "DARK BLUE"
	And showing the next panel fields and contents
	  | systemName | value          |
	  | Ankor      | -23.576.00 ISK |
