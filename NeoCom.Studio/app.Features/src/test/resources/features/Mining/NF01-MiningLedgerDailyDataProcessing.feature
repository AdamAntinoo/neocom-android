@NF01
Feature: [NF01] Process the data to be shown on the Mining Ledger Daily page.

  During the preparation phase read today extractions from the Mining Extractions repository and generate the data to
  be rendered. There are two sets of data. The aggregated list of resources extracted to be rendered on the
  header and the list of extractions aggregated by extraction location and hourly presented and evaluated.

  @NF01.01
  Scenario: [NF01.01] Aggregate the records from all the extractions of the date of today to get the list of extracted resources
	Given an empty Mining Extraction repository
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
	And today date being "2019-08-07"
	And the pilot is "92223647"
	When the resource aggregator is initialized
	Then the aggregation results contain the next data
	  | typeId | quantity | price | value  |
	  | 17459  | 23576    | 10.0  | 235760 |
	  | 17464  | 30348    | 10.0  | 303480 |
	  | 17471  | 25432    | 10.0  | 254320 |

  @NF01.02
  Scenario: [NF01.02] Read today's extractions to get the extraction systems and their aggregated resource extraction tables.
	Given an empty Mining Extraction repository
	And the next records on the MiningRepository
	  | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |
	  | 2019-08-07:14-30001740-17464-92223647 | 17464  | 30001740      | 843      | 0     | 2019-08-07         | 14             | 92223647 |
	  | 2019-08-07:14-30001740-17460-92223647 | 17460  | 30001740      | 8266     | 0     | 2019-08-07         | 14             | 92223647 |
	  | 2019-08-07:14-30001740-17453-92223647 | 17453  | 30001740      | 3345     | 0     | 2019-08-07         | 14             | 92223647 |
	  | 2019-08-07:13-30001735-17459-92223647 | 17459  | 30001735      | 38087    | 14511 | 2019-08-07         | 13             | 92223647 |
	  | 2019-08-07:12-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:12-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:12-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | 2019-08-07         | 12             | 92223647 |
	  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | 2019-08-07         | 11             | 92223647 |
	  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 1566     | 0     | 2019-08-07         | 10             | 92223647 |
	  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 421      | 0     | 2019-08-07         | 10             | 92223647 |
	And today date being "2019-08-07"
	And the pilot is "92223647"
	When the request to get today extraction records is fired
	Then there are the next system aggregators
	  | solarSystemId | solarSystemName |
	  | 30001735      | Uhodoh          |
	  | 30001740      | Arakor          |
	And system aggregator "30001735" having the next hourly records
	  | id                                    | typeId | solarSystemId | quantity | hour |
	  | 2019-08-07:13-30001735-17459-92223647 | 17459  | 30001735      | 38087    | 13   |
	  | 2019-08-07:12-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 12   |
	  | 2019-08-07:12-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 12   |
	  | 2019-08-07:12-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 12   |
	  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 11   |
	  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 11   |
	  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 1566     | 10   |
	  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 421      | 10   |
	And system aggregator "30001740" having the next hourly records
	  | id                                    | typeId | solarSystemId | quantity | hour |
	  | 2019-08-07:14-30001740-17464-92223647 | 17464  | 30001740      | 843      | 14   |
	  | 2019-08-07:14-30001740-17460-92223647 | 17460  | 30001740      | 8266     | 14   |
	  | 2019-08-07:14-30001740-17453-92223647 | 17453  | 30001740      | 3345     | 14   |

  @NF01.03
  Scenario: [NF01.03] Missed the check of data that have differences betewwen hours

  @NF01.04
  Scenario: [NF01.04] Check the collaborate2Model code