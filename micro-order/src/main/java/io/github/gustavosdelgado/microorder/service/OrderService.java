package io.github.gustavosdelgado.microorder.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.library.domain.order.Order;
import io.github.gustavosdelgado.library.domain.order.OrderRepository;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import io.github.gustavosdelgado.microorder.domain.order.OrderWebRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderWebResponse;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderRepository repository;

    public void create(OrderWebRequest request) throws BadRequestException {
        Order order = new Order(request.orderId(), request.restaurantId());
        try {
            repository.save(order);
        } catch (Exception e) {
            logger.error("Fail to create order: ", e);
            throw new BadRequestException(e);
        }
    }

    public OrderWebResponse get(Long orderId) throws NoDataFoundException {
        Optional<Order> optional = repository.findByOrderId(orderId);

        if (!optional.isPresent()) {
            throw new NoDataFoundException("Order not found");
        }

        Order order = optional.get();

        return new OrderWebResponse(order.getOrderId(), order.getRestaurantId());
    }
}
