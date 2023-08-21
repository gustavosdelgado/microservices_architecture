package io.github.gustavosdelgado.microrestaurant.controller;

import io.github.gustavosdelgado.library.domain.restaurant.Restaurant;
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
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import io.github.gustavosdelgado.library.exception.UnauthorizedException;
import io.github.gustavosdelgado.library.service.AuthTokenService;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantResponse;
import io.github.gustavosdelgado.microrestaurant.service.RestaurantService;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String RESTAURANT_ROLE = "ROLE_RESTAURANT";

    @Autowired
    private AuthTokenService tokenService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Validated @RequestBody RestaurantRequest request) {
        try {
            if (!isAuthorized(authorizationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(restaurantService.create(request, tokenService.getUserId(authorizationToken)));

        } catch (BadRequestException e) {
            logger.error("Fail to create Restaurant Entity", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Fail to create Restaurant Entity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @RequestHeader("X-Request-ID") String requestId, @PathVariable Long id) {
        try {

            logger.info("RequestID encaminhado: {}", requestId);

            if (!isAuthorized(authorizationToken)) {
                logger.info("Fail to get Restaurant Entity: unauthorized token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            RestaurantResponse response = restaurantService.get(id, tokenService.getUserId(authorizationToken));
            return ResponseEntity.ok(response);

        } catch (NoDataFoundException e) {
            logger.error("Fail to get Restaurant Entity", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (UnauthorizedException e) {
            logger.error("Fail to get Restaurant Entity", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            logger.error("Fail to get Restaurant Entity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isAuthorized(String token) {
        String role = tokenService.getRole(token);
        return RESTAURANT_ROLE.equals(role);
    }

}
