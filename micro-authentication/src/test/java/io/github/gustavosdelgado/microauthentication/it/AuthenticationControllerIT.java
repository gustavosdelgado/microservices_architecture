package io.github.gustavosdelgado.microauthentication.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private RabbitAdmin rabbitAdmin;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeAll
    public void setup() {
        restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);
    }

    @Test
    public void givenValidCredentialsWhenAuthenticateThenSuccess() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("gustavosd", "123456", null), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
    }

    @Test
    public void givenInvalidPasswordWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("gustavosd", "invalidPassword", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenInvalidUsernameWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("invalidUsername", "invalidPassword", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullCredentialWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest(null, null, null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenEmptyCredentialWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("", "", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
