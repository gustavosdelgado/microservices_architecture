package io.github.gustavosdelgado.microauthentication.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gustavosdelgado.microauthentication.domain.user.AuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void givenValidCredentialsWhenAuthenticateThenSuccess() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("gustavosd", "123456"), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void givenInvalidPasswordWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("gustavosd", "invalidPassword"), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenInvalidUsernameWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest("invalidUsername", "invalidPassword"), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenNullCredentialWhenAuthenticateThenFail() throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity("/authenticate",
                new AuthenticationRequest(null, null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
