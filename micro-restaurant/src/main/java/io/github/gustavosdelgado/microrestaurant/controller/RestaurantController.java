package io.github.gustavosdelgado.microrestaurant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;

import io.github.gustavosdelgado.microrestaurant.service.TokenService;
import reactor.core.publisher.Mono;

@RestController
public class RestaurantController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String RESTAURANT_ROLE = "ROLE_RESTAURANT";

    @Autowired
    private TokenService tokenService;

    @GetMapping("/restaurant")
    public Mono<String> getQuote(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        Claim claim = tokenService.getSubject(authorizationToken);

        if (!RESTAURANT_ROLE.equals(claim.asString())) {
            return Mono.just("UNAUTHORIZED");
        }

        return Mono.just("AUTHORIZED");
    }

}
