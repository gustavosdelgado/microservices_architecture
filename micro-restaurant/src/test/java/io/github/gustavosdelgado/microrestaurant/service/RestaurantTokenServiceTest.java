package io.github.gustavosdelgado.microrestaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@ExtendWith(MockitoExtension.class)
public class RestaurantTokenServiceTest {

    private static final String LOGIN = "login";
    private static final String SECRET = "secret";
    private static final String ROLE = "role";

    private RestaurantTokenService service;

    @Test
    void givenValidTokenWhenGetRoleThenSucceeds() {
        service = new RestaurantTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertEquals(ROLE, service.getRole("Bearer " + token), "unexpected role");
    }

    @Test
    void givenTokenWithNoBearerWhenGetRoleThenSucceeds() {
        service = new RestaurantTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertEquals(ROLE, service.getRole(token), "unexpected role");
    }

    @Test
    void givenNullTokenWhenGetRoleThenFails() {
        service = new RestaurantTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        assertNull(service.getRole(null), "unexpected role");
    }

    @Test
    void givenValidTokenWhenGetUserThenSucceeds() {
        service = new RestaurantTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertEquals(LOGIN, service.getUser("Bearer " + token), "unexpected user");
    }

    @Test
    void givenTokenWithNoBearerWhenGetUserThenSucceeds() {
        service = new RestaurantTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertEquals(LOGIN, service.getUser(token), "unexpected user");
    }

    @Test
    void givenNullTokenWhenGetUserThenFails() {
        service = new RestaurantTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        assertNull(service.getRole(null), "unexpected role");
    }
}
