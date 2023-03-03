Feature: Order management
    Scenario: User places an order
        Given a valid consumer user
        When user places an order
        Then return a created message

