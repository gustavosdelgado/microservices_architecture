Feature: Restaurant management
    Scenario: User creates a restaurant
        Given a valid restaurant user
        When user creates a restaurant
        Then return a created message