package io.github.gustavosdelgado.microrestaurant.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.library.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.library.domain.restaurant.RestaurantRepository;
import io.github.gustavosdelgado.library.domain.user.User;
import io.github.gustavosdelgado.library.domain.user.UserRepository;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantResponse;
import io.github.gustavosdelgado.microrestaurant.exception.BadRequestException;
import io.github.gustavosdelgado.microrestaurant.exception.NoDataFoundException;
import io.github.gustavosdelgado.microrestaurant.exception.UnauthorizedException;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    public void create(RestaurantRequest request, Long userId) throws BadRequestException {
        try {
            Optional<User> o = userRepository.findById(userId);
            if (o.isEmpty()) {
                throw new DataIntegrityViolationException("User not found");
            }

            Restaurant restaurant = new Restaurant(request.name(), Arrays.asList(o.get()));
            restaurantRepository.save(restaurant);

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
                return new RestaurantResponse(restaurant.getName());
            }
        }

        throw new UnauthorizedException("User not related to the restaurant");
    }

}
