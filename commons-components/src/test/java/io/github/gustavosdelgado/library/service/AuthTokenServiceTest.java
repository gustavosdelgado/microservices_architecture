package io.github.gustavosdelgado.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
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
    public static final long USER_ID = 69L;

    @InjectMocks
    private AuthTokenService service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, SECRET, SECRET);
        ReflectionTestUtils.setField(service, "tokenTimeZone", "Australia/Sydney");
        ReflectionTestUtils.setField(service, "expirationInMinutes", 15);
    }

    @Test
    void givenValidUserWhenGenerateTokenThenSucceed() {
        User user = new User(1L, "login", "password", Role.ROLE_CONSUMER);
        assertNotNull(service.gerarToken(user), "Unexpected null return");
    }

    @Test
    void givenNullUserWhenGenerateTokenThenFail() {
        assertThrows(RuntimeException.class,
                () -> service.gerarToken(null), "Expected Exception not thrown");
    }

    @Test
    void givenValidTokenWhenGetRoleThenSucceeds() {
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
        assertNull(service.getRole(null), "unexpected role");
    }

    @Test
    void givenNullTokenWhenGetUserThenFails() {
        assertNull(service.getRole(null), "unexpected role");
    }

    @Test
    void givenValidTokenWhenGetUserIdThenSucceeds() {
        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .withClaim("userId", USER_ID)
                .sign(Algorithm.HMAC512(SECRET));

        assertEquals(USER_ID, service.getUserId(token));
    }

    @Test
    void givenInvalidTokenWhenGetUserIdThenSucceeds() {
        assertNull(service.getUserId("invalidToken"));
    }

    @Test
    void givenInvalidTokenTimeZoneWhenGetRoleThenFails() {
        ReflectionTestUtils.setField(service, "tokenTimeZone", "nowhere");

        String token = JWT.create()
                .withIssuer("AuthService")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .withClaim(ROLE, ROLE)
                .sign(Algorithm.HMAC512(SECRET));

        assertThrows(Exception.class, () -> service.getRole(token), "Expected invalid time zone");
    }


}
