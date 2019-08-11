@NA07
Feature: [NA07][P0] Mining Ledger Daily mining extractions.

  This page shows the mining extractions performed during the current date. The page has the common core sections.
  The action bar for the Mining Ledger Daily will show the select pilot's name and the page label
  The page header will have a table header with the description of the column data and a list of the aggregated
  mining resources extracted during the date.
  The page data will contain renders for the different systems where the extractions have been done and for each
  system the hourly extracted data if there are extractions that have continuity over more that one hour.

  @NA07.P0.HEADER.01
  Scenario: [NA07][P0][HEADER] The header contains a table header with the labels that describe the header table data.
	Given a "MiningLedgerActivity" active at page "MINING_EXTRACTIONS_TODAY"
	When the activity completes data processing and render
	Then show a header component with 3 columns
	And being the first column for the resource name and its type identifier
	And being the second column for the aggregated extracted quantity
	And being the third column for the market sell value on default

  @NA07.P0.HEADER.02
  Scenario: [NA07][P0][HEADER] The header contains a list of extracted resources with a 3 column set of data.
	Given a "MiningLedgerActivity" active at page "MINING_EXTRACTIONS_TODAY"
	When the activity completes data processing and render
	Then show a list of "Resource" panels with the next data
	  | resourceName    | resourceId | quantity | value          |
	  | Solid Pyroxeres | [#17459]   | 23576    | -23.576.00 ISK |
