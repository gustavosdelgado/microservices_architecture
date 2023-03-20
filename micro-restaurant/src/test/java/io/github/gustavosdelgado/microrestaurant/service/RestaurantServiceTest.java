package io.github.gustavosdelgado.microrestaurant.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.gustavosdelgado.microrestaurant.domain.restaurant.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @InjectMocks
    private RestaurantService service;

    // @Test
    // public void givenValidRequestAndUserWhenCreateThenSucceeds() throws
    // BadRequestException {
    // RestaurantRequest request = new RestaurantRequest(null, "restaurantName");

    // service.create(request, "restaurantUser");

    // verify(mockRestaurantRepository).save(any());

    // verifyNoMoreInteractions(mockRestaurantRepository);
    // }

    // @Test
    // public void givenInvalidRequestAndUserWhenCreateThenFails() throws
    // BadRequestException {
    // RestaurantRequest request = new RestaurantRequest(null, "restaurantName");

    // when(mockRestaurantRepository.save(any())).thenThrow(new
    // DataIntegrityViolationException(null));
    // assertThrows(BadRequestException.class, () -> service.create(request,
    // "restaurantUser"));

    // verify(mockRestaurantRepository).save(any());

    // verifyNoMoreInteractions(mockRestaurantRepository);
    // }

    // @Test
    // public void givenValidIdAndUserWhenGetThenSucceeds() throws
    // NoDataFoundException, UnauthorizedException {
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