package io.github.gustavosdelgado.library.service;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifier.BaseVerification;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.github.gustavosdelgado.library.domain.user.User;

@Service
public class AuthTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private int expirationInMinutes;

    @Value("${api.security.token.timezone}")
    private String tokenTimeZone;

    public String gerarToken(User user) {
        try {
            if (user == null)
                throw new JWTCreationException("Null user", new RuntimeException());

            var algoritmo = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer("AuthService")
                    .withSubject(user.getLogin())
                    .withExpiresAt(calculateExpirationTime())
                    .withClaim("role", user.getRole().name())
                    .withClaim("userId", user.getId())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating token", exception);
        }
    }

    public String getRole(String tokenJWT) {
        try {
            return decodeJwt(tokenJWT).getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            LoggerFactory.getLogger(getClass()).error("Fail to verify token: ", exception);
            return null;
        }
    }

    public Long getUserId(String tokenJWT) {
        try {
            return decodeJwt(tokenJWT).getClaim("userId").asLong();
        } catch (JWTVerificationException exception) {
            LoggerFactory.getLogger(getClass()).error("Fail to verify token: ", exception);
            return null;
        }
    }

    private DecodedJWT decodeJwt(String tokenJWT) {
        if (tokenJWT == null) {
            throw new JWTVerificationException("Null token");
        }
        tokenJWT = tokenJWT.replace("Bearer ", "");
        var algoritmo = Algorithm.HMAC512(secret);
        BaseVerification verification = (BaseVerification) JWT.require(algoritmo)
                .withIssuer("AuthService");
        JWTVerifier verifier = verification.build(Clock.system(ZoneId.of(tokenTimeZone)));
        return verifier.verify(tokenJWT);
    }

    private Instant calculateExpirationTime() {
        return Clock.system(ZoneId.of(tokenTimeZone)).instant().plusSeconds(expirationInMinutes * 60);
    }
}
