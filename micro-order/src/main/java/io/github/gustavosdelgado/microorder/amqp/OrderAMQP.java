package io.github.gustavosdelgado.microorder.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OrderAMQP {

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initAdmin(RabbitAdmin admin) {
        return event -> admin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Queue createOrderCreatedQueue() {
        return QueueBuilder.durable("order.created-order").build();
    }

    @Bean
    @Qualifier("order.exchange")
    public FanoutExchange orderExchange() {
        return ExchangeBuilder.fanoutExchange("order.exchange").build();
    }

    @Bean
    public Binding bindOrderCreated(FanoutExchange exhange) {
        return BindingBuilder.bind(createOrderCreatedQueue()).to(orderExchange());
    }

    @Bean
    public Queue createUserCreatedQueue() {
        return QueueBuilder.durable("user.created-order").build();
    }

    @Bean
    @Qualifier("user.exchange")
    @Primary
    public FanoutExchange userExchange() {
        return ExchangeBuilder.fanoutExchange("user.exchange").build();
    }

    @Bean
    public Binding bindUserCreated(FanoutExchange exhange) {
        return BindingBuilder.bind(createUserCreatedQueue()).to(userExchange());
    }
}
