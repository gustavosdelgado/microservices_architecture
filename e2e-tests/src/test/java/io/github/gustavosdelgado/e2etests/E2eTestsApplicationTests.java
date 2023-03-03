package io.github.gustavosdelgado.e2etests;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


class E2eTestsApplicationTests {

	@Test
	void createRestaurant() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		HttpEntity<String> request = new HttpEntity<>(buildRequestBody("restaurant4", "12456", "ROLE_RESTAURANT"),
				headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/user", request,
				String.class);
		String json = response.getBody();
		JSONObject jsonObject = new JSONObject(json);
		String token = jsonObject.getString("token");
	}

	private String buildRequestBody(String login, String password, String role) {
		return "{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"role\": \"" + role + "\"}";
	}

}
