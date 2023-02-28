package io.github.gustavosdelgado.microrestaurant.domain.order;

import javax.validation.constraints.NotEmpty;

public record OrderRequest(@NotEmpty String orderId, @NotEmpty String restaurantId) {
    
}
