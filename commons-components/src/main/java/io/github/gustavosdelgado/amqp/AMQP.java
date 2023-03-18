package io.github.gustavosdelgado.amqp;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public interface AMQP {

    public RabbitAdmin createRabbitAdmin(ConnectionFactory factory);

    public ApplicationListener<ApplicationReadyEvent> initAdmin(RabbitAdmin admin);

    public Jackson2JsonMessageConverter messageConverter();

    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter);

}
