package io.github.gustavosdelgado.microorder.domain.order;

import javax.validation.constraints.NotNull;

import io.github.gustavosdelgado.library.controller.WebRequest;
import io.github.gustavosdelgado.library.domain.order.OrderStatus;

public record OrderWebRequest(@NotNull Long restaurantId, OrderStatus orderStatus) implements WebRequest {

}
