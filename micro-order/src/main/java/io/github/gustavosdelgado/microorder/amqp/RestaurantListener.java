package io.github.gustavosdelgado.microorder.amqp;

import io.github.gustavosdelgado.library.amqp.Listener;
import io.github.gustavosdelgado.library.domain.order.Order;
import io.github.gustavosdelgado.library.domain.order.OrderRepository;
import io.github.gustavosdelgado.library.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.library.domain.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestaurantListener implements Listener<Restaurant> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository restaurantRepository;

    @RabbitListener(queues = "restaurant.created-order")
    public void listen(Restaurant restaurant) {

        String restaurantData = """
                 Restaurant ID: %s
                 Restaurant Name: %s
                """.formatted(restaurant.getId(), restaurant.getName());

        logger.info("The following order was created: {}", restaurant);

        restaurantRepository.save(restaurant);
    }

}
