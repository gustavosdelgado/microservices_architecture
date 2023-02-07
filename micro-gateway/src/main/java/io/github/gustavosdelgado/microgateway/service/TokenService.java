package io.github.gustavosdelgado.microgateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public Claims getClaims(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }

    public void validateToken(final String token) {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }
}
