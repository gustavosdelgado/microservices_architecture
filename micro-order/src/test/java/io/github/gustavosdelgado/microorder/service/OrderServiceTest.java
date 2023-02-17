package io.github.gustavosdelgado.microorder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.gustavosdelgado.microorder.domain.order.Order;
import io.github.gustavosdelgado.microorder.domain.order.OrderRepository;
import io.github.gustavosdelgado.microorder.domain.order.OrderRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderResponse;
import io.github.gustavosdelgado.microorder.exception.BadRequestException;
import io.github.gustavosdelgado.microorder.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;

    @InjectMocks
    private OrderService service;

    @Test
    void testCreate() throws BadRequestException {
        OrderRequest request = new OrderRequest("orderId", "restaurantId");
        service.create(request);

        verify(mockRepository).save(any());

        verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testGet() throws NotFoundException {
        Order order = new Order(1L, "orderId", "restaurantId");
        when(mockRepository.findByOrderId("orderId")).thenReturn(Optional.of(order));

        OrderResponse orderResponse = service.get("orderId");
        assertEquals(1L, orderResponse.id());
        assertEquals("orderId", orderResponse.orderId());
        assertEquals("restaurantId", orderResponse.restaurantId());

        verify(mockRepository).findByOrderId("orderId");

        verifyNoMoreInteractions(mockRepository);
    }
}
