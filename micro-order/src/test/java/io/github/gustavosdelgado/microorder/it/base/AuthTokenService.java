package io.github.gustavosdelgado.microorder.it.base;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class AuthTokenService {

    public String generateToken(String secret, int expirationTime, String role) {
        try {
            var algoritmo = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer("AuthService")
                    .withSubject("login")
                    .withExpiresAt(Instant.now().plusSeconds(expirationTime))
                    .withClaim("role", role)
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating token", exception);
        }
    }

}
