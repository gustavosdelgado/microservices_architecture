package io.github.gustavosdelgado.microorder.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.github.gustavosdelgado.library.amqp.AMQP;

@Configuration
public class OrderAMQP implements AMQP {

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

    @Bean
    public Queue createRestaurantCreatedQueue() {
        return QueueBuilder.durable("restaurant.created-order").build();
    }

    @Bean
    @Qualifier("restaurant.exchange")
    public FanoutExchange restaurantExchange() {
        return ExchangeBuilder.fanoutExchange("restaurant.exchange").build();
    }

    @Bean
    public Binding bindRestaurantCreated(FanoutExchange exhange) {
        return BindingBuilder.bind(createRestaurantCreatedQueue()).to(restaurantExchange());
    }
}
