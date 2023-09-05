package io.github.gustavosdelgado.microgateway.it;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.github.gustavosdelgado.microgateway.it.base.AuthTokenService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayFilterIT {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AuthTokenService tokenService;

// TODO: extract uri configuration into environment variables
//    @Test
//    public void givenValidCredentialsAndInvalidUriWhenCallToRestaurantThenFails() {
//        WebTestClient client = WebTestClient.bindToApplicationContext(this.context).build();
//
//        var token = tokenService.generateToken();
//
//        client.get().uri("/authenticate")
//                .exchange()
//                .expectStatus()
//                .is4xxClientError();
//    }

    @Test
    public void givenNoCredentialsWhenCallToRestaurantThenFails() {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();

        client.get().uri("/restaurant")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void givenNoCredentialsWhenCallToOrderThenFails() {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();

        client.get().uri("/order")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

}
