Feature: Google Search

Scenario: Search for java tutorial
	Given User is on google.com
	When User search for "java tutorial"
	Then User displayed with google searched content