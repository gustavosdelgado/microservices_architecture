package io.github.gustavosdelgado.microrestaurant.domain.order;

import org.springframework.lang.NonNull;

public record OrderRequest(@NonNull String orderId, @NonNull String restaurantId) {
    
}
