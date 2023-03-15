package io.github.gustavosdelgado.microorder.domain.order;

import javax.validation.constraints.NotNull;

import io.github.gustavosdelgado.controller.WebRequest;

public record OrderWebRequest(@NotNull Long orderId, @NotNull Long restaurantId) implements WebRequest {

}
