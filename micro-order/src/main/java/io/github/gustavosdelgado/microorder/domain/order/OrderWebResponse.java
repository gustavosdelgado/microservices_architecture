package io.github.gustavosdelgado.microorder.domain.order;

import io.github.gustavosdelgado.library.controller.WebResponse;

public record OrderWebResponse(Long orderId, Long restaurantId) implements WebResponse {

}
