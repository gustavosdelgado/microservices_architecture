package io.github.gustavosdelgado.microrestaurant.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import io.github.gustavosdelgado.microrestaurant.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRepository;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantResponse;
import io.github.gustavosdelgado.microrestaurant.exception.BadRequestException;
import io.github.gustavosdelgado.microrestaurant.exception.NoDataFoundException;
import io.github.gustavosdelgado.microrestaurant.exception.UnauthorizedException;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public void create(RestaurantRequest request, String user) throws BadRequestException {
        try {
            Restaurant restaurant = new Restaurant(request.name(), user);
            restaurantRepository.save(restaurant);

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e);

        }
    }

    public RestaurantResponse get(Long id, String user) throws NoDataFoundException, UnauthorizedException {
        Optional<Restaurant> rOptional = restaurantRepository.findById(id);
        if (!rOptional.isPresent()) {
            throw new NoDataFoundException("Restaurant not found");
        }

        Restaurant restaurant = rOptional.get();

        if (!restaurant.getUser().equals(user)) {
            throw new UnauthorizedException("User not related to the restaurant");
        }

        return new RestaurantResponse(restaurant.getName());
    }

}
