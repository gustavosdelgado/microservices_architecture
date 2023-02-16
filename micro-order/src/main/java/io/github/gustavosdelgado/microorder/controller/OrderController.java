package io.github.gustavosdelgado.microorder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gustavosdelgado.microorder.domain.order.OrderRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderResponse;
import io.github.gustavosdelgado.microorder.exception.BadRequestException;
import io.github.gustavosdelgado.microorder.service.OrderService;
import io.github.gustavosdelgado.microorder.service.OrderTokenService;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CONSUMER_ROLE = "ROLE_CONSUMER";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService service;

    @Autowired
    private OrderTokenService tokenService;

    @PostMapping
    public ResponseEntity<String> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Validated @RequestBody OrderRequest request) {

        try {
            if (!isAuthorized(authorizationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            service.create(request);
            rabbitTemplate.convertAndSend("order.created", request);

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Fail to create order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<OrderResponse> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Validated @RequestBody OrderRequest request) {

        OrderResponse orderResponse = null;
        try {
            if (!isAuthorized(authorizationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            orderResponse = service.get(request.orderId());

        } catch (Exception e) {
            logger.error("Fail to get order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(orderResponse);
    }

    private boolean isAuthorized(String token) {
        String role = tokenService.getRole(token);
        return CONSUMER_ROLE.equals(role);
    }

}
