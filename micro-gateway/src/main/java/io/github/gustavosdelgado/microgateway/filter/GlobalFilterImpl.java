package io.github.gustavosdelgado.microgateway.filter;

import java.time.Clock;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifier.BaseVerification;
import com.auth0.jwt.algorithms.Algorithm;

import reactor.core.publisher.Mono;

@Component
public class GlobalFilterImpl implements GlobalFilter, Ordered {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${api.security.token.secret}")
    private String secret;

    private final HashSet<String> authorizedEndpoints = new HashSet<>(Arrays.asList("/authenticate", "/user"));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        RequestPath path = exchange.getRequest().getPath();

        if (authorizedEndpoints.contains(path.value())) {
            addRequestId(exchange);
            logger.info("Authentication request, forwarding...");
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        logger.info("Global Post Filter executed");
                    }));
        }

        if (!headers.containsKey("Authorization")) {
            logger.info("Authorization header absent.");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        try {
            verifyAuthorizationToken(headers);
        } catch (Exception e) {
            logger.info("Invalid authentication token.", e);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        addRequestId(exchange);

        logger.info("Global Pre Filter executed");
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    logger.info("Global Post Filter executed");
                }));
    }

    private void verifyAuthorizationToken(HttpHeaders headers) {
        var authorization = Optional.ofNullable(headers).map(
                h -> h.get("Authorization")).map(
                auth -> auth.get(0))
                .orElse("");

        var token = authorization.replace("Bearer ", "");
        BaseVerification verification = (BaseVerification) JWT.require(Algorithm.HMAC512(secret))
                .withIssuer("AuthService");
        JWTVerifier verifier = verification.build(Clock.systemUTC());
        verifier.verify(token);
    }

    private void addRequestId(ServerWebExchange exchange) {
        exchange.getRequest().mutate().header("X-Request-ID", UUID.randomUUID().toString());
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
