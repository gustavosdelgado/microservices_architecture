package io.github.gustavosdelgado.e2etests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberSteps {

    private String token;
    private ResponseEntity<String> response;
    private RestTemplate restTemplate;
    private HttpHeaders headers;

    @Before
    public void setup() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    }

    @Given("a valid restaurant user")
    public void a_valid_restaurant_user() {
        HttpEntity<String> request = new HttpEntity<>(buildRequestBody("restaurant", "123456", "ROLE_RESTAURANT"),
                headers);
        response = restTemplate.postForEntity("http://localhost:8080/user", request,
                String.class);
        String json = response.getBody();
        JSONObject jsonObject = new JSONObject(json);
        token = jsonObject.getString("token");
    }

    @Given("a valid consumer user")
    public void a_valid_consumer_user() {
        HttpEntity<String> request = new HttpEntity<>(buildRequestBody("consumer", "123456", "ROLE_CONSUMER"),
                headers);
        response = restTemplate.postForEntity("http://localhost:8080/user", request,
                String.class);
        String json = response.getBody();
        JSONObject jsonObject = new JSONObject(json);
        token = jsonObject.getString("token");
    }

    @When("user places an order")
    public void user_places_an_order() {
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>("{\"restaurantId\":\"1\"}",
                headers);
        response = restTemplate.postForEntity("http://localhost:8080/order", request,
                String.class);
    }

    @When("user creates a restaurant")
    public void user_creates_a_restaurant() {
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>("{\"name\":\"Flor de Manjericao\"}",
                headers);
        response = restTemplate.postForEntity("http://localhost:8080/restaurant", request,
                String.class);
    }

    @Then("return a created message")
    public void return_a_created_message() {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private String buildRequestBody(String login, String password, String role) {
        return "{\"login\":\"" + login + RandomStringUtils.randomNumeric(3) +  "\",\"password\":\"" + password + "\",\"role\": \"" + role + "\"}";
    }

}
