package io.github.gustavosdelgado.microrestaurant.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.gustavosdelgado.microrestaurant.domain.user.User;
import io.github.gustavosdelgado.microrestaurant.domain.user.UserRepository;

@Component
public class UserListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = "user.created-restaurant")
    public void syncCreatedUser(User userReceived) {

        String userData = """
                 User ID: %s
                 Login: %s
                """.formatted(userReceived.getId(), userReceived.getLogin());

        logger.info("The following user was received: {}", userData);

        userRepository.save(userReceived);
    }

}