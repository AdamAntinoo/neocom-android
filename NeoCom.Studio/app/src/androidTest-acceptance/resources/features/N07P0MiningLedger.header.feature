@MiningLedgerActivity
Feature: Shows a table header and then the list of extracted resources during today's date.

  Scenario: [N07][P0][HEADER] List of today's extractions resources. Table header
	Given the activity MiningLedgerActivity
	When the activity MiningLedgerActivity is started
	Then there is a header panel of type TableHeader
	And that panel has 3 labels
	And the label "1" contains the literal "RESOURCE NAME [#TYPEID]"
	And the label "2" contains the literal "QUANTITY"
	And the label "3" contains the literal "VALUE"

  Scenario: [N07][P0][HEADER] List of today's extractions resources. Resource list
	Given the activity MiningLedgerActivity
	When the activity MiningLedgerActivity is started
	Then there is a list of header panels of type Resource
	And that each panel has an icon representing the resource icon
	And i get the next list of resource data
	  | resourceName | typeId | quantity |
	  | Tritanium    | 34     | 1200     |
	  | Plagioclase  | 18     | 400      |
