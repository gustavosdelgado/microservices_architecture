package io.github.gustavosdelgado.microgateway.it.base;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class AuthTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken() {
        try {
            var algoritmo = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer("AuthService")
                    .withSubject("login")
                    .withExpiresAt(Instant.now().plusSeconds(60))
                    .withClaim("role", "role")
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating token", exception);
        }
    }

}
