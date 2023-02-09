package io.github.gustavosdelgado.microrestaurant.service;

import java.time.Clock;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifier.BaseVerification;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;

@Service
public class RestaurantTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public Claim getRole(String tokenJWT) {
        try {
            tokenJWT = tokenJWT.replace("Bearer ", "");
            var algoritmo = Algorithm.HMAC512(secret);
            BaseVerification verification = (BaseVerification) JWT.require(algoritmo)
                    .withIssuer("AuthService");
            JWTVerifier verifier = verification.build(Clock.systemUTC());
            return verifier.verify(tokenJWT).getClaim("role");
        } catch (JWTVerificationException exception) {
            LoggerFactory.getLogger(getClass()).error("Fail to verify token: ", exception);
            return null;
        }
    }

    public String getUser(String tokenJWT) {
        try {
            tokenJWT = tokenJWT.replace("Bearer ", "");
            var algoritmo = Algorithm.HMAC512(secret);
            BaseVerification verification = (BaseVerification) JWT.require(algoritmo)
                    .withIssuer("AuthService");
            JWTVerifier verifier = verification.build(Clock.systemUTC());
            return verifier.verify(tokenJWT).getSubject();
        } catch (JWTVerificationException exception) {
            LoggerFactory.getLogger(getClass()).error("Fail to verify token: ", exception);
            return null;
        }
    }
}
