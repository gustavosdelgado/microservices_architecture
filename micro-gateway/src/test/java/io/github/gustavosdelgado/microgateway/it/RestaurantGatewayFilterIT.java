package io.github.gustavosdelgado.microgateway.it;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.github.gustavosdelgado.microgateway.it.base.AuthTokenService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantGatewayFilterIT {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AuthTokenService tokenService;

    @Test
    public void givenValidCredentialsAndInvalidUriWhenCallToRestaurantThenFails() throws Exception {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context).build();

        var token = tokenService.generateToken();

        client.get().uri("/notFound").header("Authorization", token).exchange().expectStatus().is4xxClientError();
    }

    @Test
    public void givenNoCredentialsWhenCallToRestaurantThenFails() throws Exception {
        WebTestClient client = WebTestClient.bindToApplicationContext(this.context)
                .build();
        client.get().uri("/restaurant").exchange().expectStatus().isUnauthorized();
    }

}
