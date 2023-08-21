package io.github.gustavosdelgado.microorder.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.library.controller.Controller;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import io.github.gustavosdelgado.library.service.AuthTokenService;
import io.github.gustavosdelgado.microorder.domain.order.OrderWebRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderWebResponse;
import io.github.gustavosdelgado.microorder.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController implements Controller<OrderWebRequest, OrderWebResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CONSUMER_ROLE = "ROLE_CONSUMER";

    @Autowired
    private OrderService service;

    @Autowired
    private AuthTokenService tokenService;

    @PostMapping
    public ResponseEntity<OrderWebResponse> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Validated @RequestBody OrderWebRequest request) {

        try {
            if (!isAuthorized(authorizationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.ok(service.create(request));

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Fail to create order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderWebResponse> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @PathVariable Long orderId) {

        try {
            if (!isAuthorized(authorizationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            OrderWebResponse orderResponse = service.get(orderId);
            return ResponseEntity.ok(orderResponse);

        } catch (NoDataFoundException e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<OrderWebResponse>> list(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            Pageable pageable) {

        try {
            if (!isAuthorized(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<OrderWebResponse> list = service.list(pageable);
            return ResponseEntity.ok(list);

        } catch (NoDataFoundException e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<OrderWebResponse> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable Long orderId) {

        try {
            if (!isAuthorized(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            service.removeById(orderId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderWebResponse> update(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Validated @RequestBody OrderWebRequest request, @PathVariable Long orderId) {
        try {
            if (!isAuthorized(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            OrderWebResponse response = service.update(orderId, request);
            return ResponseEntity.ok(response);

        } catch (NoDataFoundException e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public boolean isAuthorized(String token) {
        String role = tokenService.getRole(token);
        return CONSUMER_ROLE.equals(role);
    }

}
