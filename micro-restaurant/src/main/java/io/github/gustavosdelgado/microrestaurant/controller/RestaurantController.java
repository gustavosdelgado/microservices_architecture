package io.github.gustavosdelgado.microrestaurant.controller;

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

import com.auth0.jwt.interfaces.Claim;

import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantResponse;
import io.github.gustavosdelgado.microrestaurant.exception.BadRequestException;
import io.github.gustavosdelgado.microrestaurant.exception.NoDataFoundException;
import io.github.gustavosdelgado.microrestaurant.exception.UnauthorizedException;
import io.github.gustavosdelgado.microrestaurant.service.RestaurantService;
import io.github.gustavosdelgado.microrestaurant.service.RestaurantTokenService;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String RESTAURANT_ROLE = "ROLE_RESTAURANT";

    @Autowired
    private RestaurantTokenService tokenService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<String> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Validated @RequestBody RestaurantRequest request) {
        try {
            if (!isAuthorized(authorizationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            restaurantService.create(request, tokenService.getUser(authorizationToken));
            return ResponseEntity.status(HttpStatus.CREATED).build();

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            RestaurantResponse response = restaurantService.get(id, tokenService.getUser(authorizationToken));
            return ResponseEntity.ok(response);

        } catch (NoDataFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            logger.error("Fail to get Restaurant Entity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isAuthorized(String token) {
        Claim claim = tokenService.getRole(token);
        return RESTAURANT_ROLE.equals(claim.asString());
    }

}
