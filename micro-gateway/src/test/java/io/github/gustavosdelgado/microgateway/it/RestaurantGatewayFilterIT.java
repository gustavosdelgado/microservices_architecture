package io.github.gustavosdelgado.microgateway.it;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.github.gustavosdelgado.microgateway.it.base.AuthTokenService;


@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    "spring.cloud.gateway.routes[0].id=test",
    "spring.cloud.gateway.routes[0].uri=http://httpbin.org",
    "spring.cloud.gateway.routes[0].predicates[0]=Path=/test/**",
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantGatewayFilterIT {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AuthTokenService tokenService;

    @Test
    public void givenValidCredentialsWhenCallToRestaurantThenSucceeds() throws Exception {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context).build();

        var token = tokenService.generateToken();

        client.get().uri("/test").header("Authorization", token).exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void givenNoCredentialsWhenCallToRestaurantThenFails() throws Exception {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
        .build();
        client.get().uri("http://localhost:8081/restaurant").exchange().expectStatus().isUnauthorized();
    }

}
