package io.github.gustavosdelgado.microorder.domain.order;

import javax.validation.constraints.NotEmpty;

public record OrderRequest(@NotEmpty String orderId, @NotEmpty String restaurantId) {
    
}
