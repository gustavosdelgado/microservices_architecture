package io.github.gustavosdelgado.library.domain.order;

import io.github.gustavosdelgado.library.controller.WebResponse;

public record OrderWebResponse(Long orderId, Long restaurantId) implements WebResponse {

}
