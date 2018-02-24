Feature: Gmail Login

Scenario: Gmail login successfully
	Given User is on www.gmail.com
	When User enters email id
	And Enters password
	Then User redirects gmail home page