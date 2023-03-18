package io.github.gustavosdelgado.microauthentication.amqp;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.gustavosdelgado.library.amqp.AMQP;

@Configuration
public class UserAMQP implements AMQP {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("user.exchange");
    }
}
