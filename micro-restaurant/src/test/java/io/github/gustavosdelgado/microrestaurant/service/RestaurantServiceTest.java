package io.github.gustavosdelgado.microrestaurant.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRepository;
import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRequest;
import io.github.gustavosdelgado.microrestaurant.exception.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @InjectMocks
    private RestaurantService service;

    @Test
    public void givenValidRequestAndUserWhenCreateThenSucceeds() throws BadRequestException {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("restaurantName");

        service.create(request, "restaurantUser");

        verify(mockRestaurantRepository).save(any());

        verifyNoMoreInteractions(mockRestaurantRepository);
    }

    @Test
    public void givenInvalidRequestAndUserWhenCreateThenFails() throws BadRequestException {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("restaurantName");

        when(mockRestaurantRepository.save(any())).thenThrow(new DataIntegrityViolationException(null));
        assertThrows(BadRequestException.class, () -> service.create(request, "restaurantUser"));

        verify(mockRestaurantRepository).save(any());

        verifyNoMoreInteractions(mockRestaurantRepository);
    }

    // @Test
    // public void givenValidIdAndUserWhenGetThenSucceeds() throws
    // NoDataFoundException {
    // Restaurant restaurant = new Restaurant(1L, "name", "user");
    // Optional<Restaurant> rOptional = Optional.of(restaurant);
    // when(mockRestaurantRepository.findById(1L)).thenReturn(rOptional);

    // assertEquals(restaurant.getName(), service.get(1L, "user").getName());
    // }

    // @Test
    // public void givenRestaurantNotFoundWhenGetThenFails() throws
    // NoDataFoundException {
    // Optional<Restaurant> rOptional = Optional.ofNullable(null);
    // when(mockRestaurantRepository.findById(1L)).thenReturn(rOptional);

    // assertThrows(NoDataFoundException.class, () -> service.get(1L,
    // "user").getName());
    // }

    // @Test
    // public void givenInvalidUserWhenGetThenFails() throws NoDataFoundException {
    // Restaurant restaurant = new Restaurant(1L, "name", "user");
    // Optional<Restaurant> rOptional = Optional.of(restaurant);
    // when(mockRestaurantRepository.findById(1L)).thenReturn(rOptional);

    // assertThrows(UnauthorizedException.class, () -> service.get(1L,
    // "invalidUser").getName());
    // }

}
