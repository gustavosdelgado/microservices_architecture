package io.github.gustavosdelgado.microrestaurant.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class RestaurantController {

    // private static final GrantedAuthority GOLD_CUSTOMER = new
    // SimpleGrantedAuthority("restaurant");

    @GetMapping("/restaurant")
    public Mono<String> getQuote() {
        return Mono.just("UNAUTHORIZED");
    }

}
