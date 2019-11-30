@NC05 @AuthorizationFlowActivity
Feature: [NC05]-The authorization flow activity will be used to authorize new credentials over the ESI oauth v1 service.

  To add new credentials to the NeoCom application I should use the ESI OAuth authentication portal to generate the correct
  credentials. Those are stored on the Credential instance and persisted on the local application repository.

  @NC05.01
  Scenario: [NC05.01]-The authorization flow is started with no intents. The page should show a message and then go back to the Splash.
	Given the AuthorizationFlowActivity
	When the AuthorizationFlowActivity is created with no intent
	Then a InformationPanel is visible with the next information
	  | WaitingMessage                                                                              |
	  | Login activation launch is not correct. Try again from the pilot credential selection page. |

  @NC05.02
  Scenario: [NC05.02]-The authorization flow is started with an Intent to compose and fire the login oauth flow by calling the ESI login service.
	Given the AuthorizationFlowActivity
	When the AuthorizationFlowActivity is created with the FIRE_AUTHORIZATION_FLOW intent
	Then one external call is issued to the esi login server
	And when the authorization completes then a new Credential is created with the next data
	  | UniqueId               | AccountId  | AccountName | RaceName |
	  | tranquility/2113197470 | 2113197470 | Tip Tophane | Caldari  |
