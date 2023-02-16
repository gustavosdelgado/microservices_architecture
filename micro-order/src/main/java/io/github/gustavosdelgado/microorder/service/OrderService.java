package io.github.gustavosdelgado.microorder.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.microorder.domain.order.Order;
import io.github.gustavosdelgado.microorder.domain.order.OrderRepository;
import io.github.gustavosdelgado.microorder.domain.order.OrderRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderResponse;
import io.github.gustavosdelgado.microorder.exception.BadRequestException;
import io.github.gustavosdelgado.microorder.exception.NotFoundException;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderRepository repository;

    public void create(OrderRequest request) throws BadRequestException {
        Order order = new Order(request.orderId(), request.restaurantId());
        try {
            repository.save(order);
        } catch (Exception e) {
            logger.error("Fail to create order: ", e);
            throw new BadRequestException();
        }
    }

    public OrderResponse get(String orderId) throws NotFoundException {
        Optional<Order> optional = repository.findByOrderId(orderId);

        if (!optional.isPresent()) {
            throw new NotFoundException("Order not found");
        }

        Order order = optional.get();

        return new OrderResponse(order.getId(), order.getOrderId(), order.getRestaurantId());
    }
}
