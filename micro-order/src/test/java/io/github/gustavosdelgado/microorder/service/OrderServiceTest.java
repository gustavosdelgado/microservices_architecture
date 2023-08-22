package io.github.gustavosdelgado.microorder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import io.github.gustavosdelgado.library.domain.order.OrderStatus;
import io.github.gustavosdelgado.library.domain.restaurant.Restaurant;
import io.github.gustavosdelgado.library.domain.restaurant.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import io.github.gustavosdelgado.library.domain.order.Order;
import io.github.gustavosdelgado.library.domain.order.OrderRepository;
import io.github.gustavosdelgado.library.exception.BadRequestException;
import io.github.gustavosdelgado.library.exception.NoDataFoundException;
import io.github.gustavosdelgado.microorder.domain.order.OrderWebRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderWebResponse;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService service;

    @Test
    void testCreate() throws BadRequestException {
        OrderWebRequest request = new OrderWebRequest(54321L, OrderStatus.CONFIRMATION_PENDING);

        Restaurant restaurant = new Restaurant();
        when(mockRestaurantRepository.findById(request.restaurantId())).thenReturn(Optional.of(restaurant));
        service.create(request);

        verify(mockRepository).save(any());
        verify(mockRestaurantRepository).findById(request.restaurantId());

        verifyNoMoreInteractions(mockRepository, mockRestaurantRepository);
    }

    @Test
    void testGet() throws NoDataFoundException {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(54321L);
        Order order = new Order(12345L, restaurant, OrderStatus.CONFIRMATION_PENDING);
        when(mockRepository.findByOrderId(12345L)).thenReturn(Optional.of(order));

        OrderWebResponse orderResponse = service.get(12345L);
        assertEquals(12345L, orderResponse.orderId());
        assertEquals(54321L, orderResponse.restaurantId());

        verify(mockRepository).findByOrderId(12345L);

        verifyNoMoreInteractions(mockRepository);
    }
}
