package io.github.gustavosdelgado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.github.gustavosdelgado.library.domain.user.Role;
import io.github.gustavosdelgado.library.domain.user.User;
import io.github.gustavosdelgado.library.service.AuthTokenService;

@ExtendWith(MockitoExtension.class)
public class AuthTokenServiceTest {

    private static final String LOGIN = "login";
    private static final String SECRET = "secret";
    private static final String ROLE = "role";

    @InjectMocks
    private AuthTokenService service;

    @Test
    void givenValidUserWhenGenerateTokenThenSucceed() {
        User user = new User(1L, "login", "password", Role.ROLE_CONSUMER);
        ReflectionTestUtils.setField(service, "secret", "secret");
        ReflectionTestUtils.setField(service, "expirationInMinutes", 15);
        assertNotNull(service.gerarToken(user), "Unexpected null return");
    }

    @Test
    void givenNullUserWhenGenerateTokenThenFail() {
        ReflectionTestUtils.setField(service, "secret", "secret");
        assertThrows(RuntimeException.class,
                () -> service.gerarToken(null), "Expected Exception not thrown");
    }

    @Test
    void givenValidTokenWhenGetRoleThenSucceeds() {
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
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        assertNull(service.getRole(null), "unexpected role");
    }

    @Test
    void givenValidTokenWhenGetUserThenSucceeds() {
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
        ReflectionTestUtils.setField(service, SECRET, SECRET);

        assertNull(service.getRole(null), "unexpected role");
    }
}
