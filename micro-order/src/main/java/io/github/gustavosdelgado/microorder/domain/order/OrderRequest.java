package io.github.gustavosdelgado.microorder.domain.order;

import org.springframework.lang.NonNull;

public record OrderRequest(@NonNull String orderId, @NonNull String restaurantId) {
    
}
