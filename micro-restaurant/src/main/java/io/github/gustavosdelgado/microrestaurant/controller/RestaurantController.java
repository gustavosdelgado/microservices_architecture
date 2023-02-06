package io.github.gustavosdelgado.microrestaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class RestaurantController {

    // private static final GrantedAuthority GOLD_CUSTOMER = new
    // SimpleGrantedAuthority("restaurant");

    @GetMapping("/restaurant")
    public Mono<String> getQuote() {
        // if (auth.getAuthorities().contains(GOLD_CUSTOMER)) {
        // return Mono.just("AUTHORIZED");
        // }
        return Mono.just("UNAUTHORIZED");
    }

}
