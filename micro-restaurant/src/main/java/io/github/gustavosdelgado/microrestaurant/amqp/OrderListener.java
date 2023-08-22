package io.github.gustavosdelgado.microrestaurant.amqp;

import io.github.gustavosdelgado.library.domain.order.Order;
import io.github.gustavosdelgado.library.domain.order.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.gustavosdelgado.library.amqp.Listener;
import io.github.gustavosdelgado.microrestaurant.domain.order.OrderRequest;

@Component
public class OrderListener implements Listener<Order> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = "order.created-restaurant")
    public void listen(Order order) {

        String orderData = """
                 Order ID: %s
                 Restaurant ID: %s
                """.formatted(order.getOrderId(), order.getRestaurant().getId());

        logger.info("The following order was created: {}", orderData);

        orderRepository.save(order);
    }

}
