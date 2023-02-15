package io.github.gustavosdelgado.microrestaurant.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(queues = "order.created")
    public void receiveMessage(Message message) {
        logger.info("No restaurante foi recebida a mensagem: {}", new String(message.getBody()));
    }

}
