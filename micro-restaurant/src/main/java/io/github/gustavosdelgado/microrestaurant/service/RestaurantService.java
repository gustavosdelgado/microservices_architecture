package io.github.gustavosdelgado.microrestaurant.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.library.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.library.domain.restaurant.RestaurantRepository;
import io.github.gustavosdelgado.library.domain.user.User;
import io.github.gustavosdelgado.library.domain.user.UserRepository;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import io.github.gustavosdelgado.library.exception.UnauthorizedException;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantResponse;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public RestaurantResponse create(RestaurantRequest request, Long userId) throws BadRequestException {
        try {
            Optional<User> o = userRepository.findById(userId);
            if (o.isEmpty()) {
                throw new DataIntegrityViolationException("User not found");
            }

            Restaurant restaurant = new Restaurant(request.name(), List.of(o.get()));
            restaurant = restaurantRepository.save(restaurant);

            rabbitTemplate.convertAndSend("restaurant.exchange", "", restaurant);
            return new RestaurantResponse(restaurant.getName(), restaurant.getId());

        } catch (DataAccessException e) {
            throw new BadRequestException(e);

        }
    }

    public RestaurantResponse get(Long id, Long userId) throws NoDataFoundException, UnauthorizedException {
        Optional<Restaurant> rOptional = restaurantRepository.findById(id);
        if (!rOptional.isPresent()) {
            throw new NoDataFoundException("Restaurant not found");
        }

        Restaurant restaurant = rOptional.get();

        for (User user : restaurant.getUsers()) {
            if (user.getId().compareTo(userId) == 0) {
                return new RestaurantResponse(restaurant.getName(), restaurant.getId());
            }
        }

        throw new UnauthorizedException("User not related to the restaurant");
    }

}
