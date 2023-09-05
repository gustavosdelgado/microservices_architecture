package io.github.gustavosdelgado.microauthentication.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private RabbitAdmin rabbitAdmin;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    public void givenValidCredentialsWhenCreateUserThenSuccess() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
    }

    @Test
    public void givenRepeatedUsernameWhenCreateUserThenFails() {
        restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);

        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullUsernameWhenCreateUserThenFails() {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest(null, "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenEmptyUsernameWhenCreateUserThenFails() {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("", "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullPasswordWhenCreateUserThenFails() {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", null, Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenEmptyPasswordWhenCreateUserThenFails() {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullRoleWhenCreateUserThenSuccess() {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
