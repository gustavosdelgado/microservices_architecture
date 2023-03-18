package io.github.gustavosdelgado.microrestaurant.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import io.github.gustavosdelgado.amqp.Listener;
import io.github.gustavosdelgado.microrestaurant.domain.order.OrderRequest;

@Component
public class OrderListener implements Listener<OrderRequest> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(queues = "order.created-restaurant")
    public void listen(OrderRequest request) {

        String orderData = """
                 Order ID: %s
                 Restaurant ID: %s
                """.formatted(request.orderId(), request.restaurantId());

        logger.info("No restaurante foi recebida a mensagem: {}", orderData);

    }

}
