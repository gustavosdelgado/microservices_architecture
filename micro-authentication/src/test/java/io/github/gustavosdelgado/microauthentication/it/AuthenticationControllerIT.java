package io.github.gustavosdelgado.microauthentication.it;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private RabbitAdmin rabbitAdmin;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeAll
    void setup() {
        restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);
    }

    @Test
    void givenValidCredentialsWhenAuthenticateThenSuccess() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("gustavosd", "123456", null), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
    }

    @Test
    void givenInvalidPasswordWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("gustavosd", "invalidPassword", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void givenInvalidUsernameWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("invalidUsername", "invalidPassword", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void givenNullCredentialWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest(null, null, null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void givenEmptyCredentialWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("", "", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
