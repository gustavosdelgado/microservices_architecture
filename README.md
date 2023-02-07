# Microservices Architecture Project

## Food Delivery Application Project

### This is a simple food delivery application created with the intention of applying my knowledge about microservices architecture.

## 1. Prerequisites

### Before you continue, make sure you have installed the latest version of Docker.

## 2. How to use it

### In order to use the application, just clone the project and run the following command on the root:

`docker compose up`

## 3. Documentation

## 3.1 Stack used:

 - OpenJDK 17;
 - Spring Cloud Gateway;
 - Spring Boot;
 - Spring Security
 - Spring Web;
 - Spring Webflux;
 - MySQL 5.7;

## 3.2 System Requirements by Example

### Feature: User creates an account
    Scenario: User tries to create an account
        Given an unauthenticated user
        
        When the user asks to create an account
            And the user inputs email, password and role
            And the email is not already used
            And the password is valid
            And the role is valid

        Then the user should be created
            And the application should answer with a successful created response

### Feature: User creates restaurant
    Scenario: User authenticated and authorized tries to create a restaurant
        Given an authenticated user
            And an authorized RESTAURANT_ROLE user
        
        When the user asks to create a restaurant

        Then the restaurant should be created
            And the restaurant created should be attached to the user responsible for the creation

### Feature: User edits restaurant
    Scenario: User authenticated and authorized tries to edit a restaurant
        Given an authenticated user
            And an authorized RESTAURANT_ROLE user
        
        When the user asks to edit a restaurant

        Then the restaurant should be edited
            And the application should answer with a sucessful OK response

### Feature: Consumer makes an order
    Scenario: User authenticated and authorized tries to create an order on a restaurant
        Given an authenticated user
            And an authorized CONSUMER_ROLE user
            And a restaurant available within the user geospacial limits
        
        When the user asks to create an order

        Then an order should be created with the CREATE_PENDING status
            And the restaurant should be notified
            And the application should answer with a sucessful created response

    Scenario: Restaurant acknowledges an order 
        Given an authenticated user
            And an authorized RESTAURANT_ROLE user
            And a restaurant related to the user
            And a CREATE_PENDING status order is related to the restaurant
        
        When the user acknowledges the order

        Then the application should answer with a successful OK response
            And the order status should change to PREPARE_PENDING
