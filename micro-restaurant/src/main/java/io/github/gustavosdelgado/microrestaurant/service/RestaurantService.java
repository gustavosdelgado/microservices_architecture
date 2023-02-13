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

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public void create(RestaurantRequest request, String user) throws BadRequestException {
        try {
            Restaurant restaurant = new Restaurant(request.getName(), user);
            restaurantRepository.save(restaurant);

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e);

        }
    }

    public RestaurantResponse get(Long id, String user) throws NoDataFoundException {
        Optional<Restaurant> rOptional = restaurantRepository.findbyIdAndUser(id, user);
        if (!rOptional.isPresent()) {
            throw new NoDataFoundException("Restaurant not found");
        }

        Restaurant restaurant = rOptional.get();

        return new RestaurantResponse(restaurant.getName());
    }

}
