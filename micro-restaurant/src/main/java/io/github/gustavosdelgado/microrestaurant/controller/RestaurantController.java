package io.github.gustavosdelgado.microrestaurant.controller;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.Claim;

import io.github.gustavosdelgado.microrestaurant.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRepository;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantResponse;
import io.github.gustavosdelgado.microrestaurant.service.RestaurantTokenService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String RESTAURANT_ROLE = "ROLE_RESTAURANT";

    @Autowired
    private RestaurantTokenService tokenService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping
    public ResponseEntity<String> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Validated @RequestBody RestaurantRequest request) {
        if (!isAuthorized(authorizationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Restaurant restaurant = new Restaurant(request.getName(), tokenService.getUser(authorizationToken));
        restaurantRepository.save(restaurant);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @RequestParam String id) {
        if (!isAuthorized(authorizationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private boolean isAuthorized(String token) {
        Claim claim = tokenService.getRole(token);
        if (!RESTAURANT_ROLE.equals(claim.asString())) {
            return false;
        }
        return true;
    }

}
