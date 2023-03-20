package io.github.gustavosdelgado.microorder.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void create(OrderWebRequest request) throws BadRequestException {
        Order order = new Order(request.orderId(), request.restaurantId());
        try {
            repository.save(order);
            rabbitTemplate.convertAndSend("order.exchange", "", request);
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

    public List<OrderWebResponse> list(Pageable pageable) throws NoDataFoundException {
        Page<Order> orders = repository.findAll(pageable);

        if (!orders.hasContent()) {
            throw new NoDataFoundException("Order not found");
        }

        List<Order> content = orders.getContent();

        return content.stream()
                .map(c -> new OrderWebResponse(c.getOrderId(), c.getRestaurantId()))
                .collect(Collectors.toList());
    }

    public void removeById(Long orderId) {
        repository.deleteById(orderId);
    }

    public OrderWebResponse update(Long orderId, OrderWebRequest request) throws NoDataFoundException {
        Optional<Order> optional = repository.findById(orderId);

        if (!optional.isPresent()) {
            throw new NoDataFoundException("Order not found");
        }

        Order order = optional.get();

        order.setOrderId(request.orderId());
        order.setRestaurantId(request.restaurantId());

        repository.save(order);

        return new OrderWebResponse(order.getOrderId(), order.getRestaurantId());
    }

}
