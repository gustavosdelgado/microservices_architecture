package io.github.gustavosdelgado.microauthentication.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
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
    public void givenRepeatedUsernameWhenCreateUserThenFails() throws Exception {
        restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);

        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullUsernameWhenCreateUserThenFails() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest(null, "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenEmptyUsernameWhenCreateUserThenFails() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("", "123456", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullPasswordWhenCreateUserThenFails() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", null, Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenEmptyPasswordWhenCreateUserThenFails() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "", Role.ROLE_CONSUMER), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullRoleWhenCreateUserThenSuccess() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/user",
                new AuthenticationRequest("gustavosd", "123456", null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
