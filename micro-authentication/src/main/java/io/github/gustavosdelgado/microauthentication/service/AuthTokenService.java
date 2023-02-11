package io.github.gustavosdelgado.microauthentication.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import io.github.gustavosdelgado.microauthentication.domain.user.User;

@Service
public class AuthTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private int expirationInMinutes;

    public String gerarToken(User user) {
        try {
            if (user == null)
                throw new JWTCreationException("Null user", new RuntimeException());

            var algoritmo = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer("AuthService")
                    .withSubject(user.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .withClaim("role", user.getRole().name())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating token", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusMinutes(expirationInMinutes).toInstant(ZoneOffset.UTC);
    }
}
