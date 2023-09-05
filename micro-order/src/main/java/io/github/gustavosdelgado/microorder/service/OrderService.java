package io.github.gustavosdelgado.microorder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.gustavosdelgado.library.domain.order.OrderStatus;
import io.github.gustavosdelgado.library.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.library.domain.restaurant.RestaurantRepository;
import org.apache.commons.lang3.RandomStringUtils;
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
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public OrderWebResponse create(OrderWebRequest request) throws BadRequestException {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(request.restaurantId());

        try {
            if (restaurantOptional.isEmpty()) {
                throw new NoDataFoundException("Restaurant not found");
            }

            Restaurant restaurant = restaurantOptional.get();
            Order order = new Order(Long.parseLong(RandomStringUtils.randomNumeric(16)),
                    restaurant, OrderStatus.CONFIRMATION_PENDING);
            Optional.ofNullable(restaurant.getOrders()).orElse(new ArrayList<>()).add(order);

            restaurantRepository.save(restaurant);
            rabbitTemplate.convertAndSend("order.exchange", "", order);

            return new OrderWebResponse(order.getOrderId(), order.getRestaurant().getId());
        } catch (Exception e) {
            logger.error("Fail to create order: ", e);
            throw new BadRequestException(e);
        }
    }

    public OrderWebResponse get(Long orderId) throws NoDataFoundException {
        Optional<Order> optional = orderRepository.findByOrderId(orderId);

        if (optional.isEmpty()) {
            throw new NoDataFoundException("Order not found");
        }

        Order order = optional.get();

        return new OrderWebResponse(order.getOrderId(), order.getRestaurant().getId());
    }

    public List<OrderWebResponse> list(Pageable pageable) throws NoDataFoundException {
        Page<Order> orders = orderRepository.findAll(pageable);

        if (!orders.hasContent()) {
            throw new NoDataFoundException("Order not found");
        }

        List<Order> content = orders.getContent();

        return content.stream()
                .map(c -> new OrderWebResponse(c.getOrderId(), c.getRestaurant().getId()))
                .collect(Collectors.toList());
    }

    public void removeById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public OrderWebResponse update(Long orderId, OrderWebRequest request) throws NoDataFoundException {
        Optional<Order> optional = orderRepository.findById(orderId);

        if (optional.isEmpty()) {
            throw new NoDataFoundException("Order not found");
        }

        Order order = optional.get();
        order.setOrderStatus(request.orderStatus());
        orderRepository.save(order);

        return new OrderWebResponse(order.getOrderId(), order.getRestaurant().getId());
    }

}
