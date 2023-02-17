package io.github.gustavosdelgado.microorder.service;

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
public class OrderTokenServiceTest {

    private static final String LOGIN = "login";
    private static final String SECRET = "secret";
    private static final String ROLE = "role";

    private OrderTokenService service;

    @Test
    void givenValidTokenWhenGetRoleThenSucceeds() {
        service = new OrderTokenService();
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
    void givenValidTokenWithDifferentClaimWhenGetRoleThenFails() {
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim("differentClaim", ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertNull(service.getRole("Bearer " + token), "unexpected role");
    }

    @Test
    void givenValidTokenWithDifferentSecretWhenGetRoleThenFails() {
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512("differentSecret"));

        assertNull(service.getRole("Bearer " + token), "unexpected role");
    }

    @Test
    void givenValidTokenWithDifferentIssuerWhenGetRoleThenFails() {
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("differentIssuer")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertNull(service.getRole("Bearer " + token), "unexpected role");
    }

    @Test
    void givenTokenWithNoBearerWhenGetRoleThenSucceeds() {
        service = new OrderTokenService();
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
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        assertNull(service.getRole(null), "unexpected role");
    }

    @Test
    void givenValidTokenWhenGetUserThenSucceeds() {
        service = new OrderTokenService();
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
    void givenValidTokenWithDifferentSecretWhenGetUserThenFails() {
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512("differentSecret"));

        assertNull(service.getUser("Bearer " + token), "unexpected role");
    }

    @Test
    void givenValidTokenWithDifferentIssuerWhenGetUserThenFails() {
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        String token = JWT.create()
                .withIssuer("differentIssuer")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertNull(service.getUser("Bearer " + token), "unexpected user");
    }

    @Test
    void givenTokenWithNoBearerWhenGetUserThenSucceeds() {
        service = new OrderTokenService();
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
        service = new OrderTokenService();
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        assertNull(service.getRole(null), "unexpected role");
    }
}
