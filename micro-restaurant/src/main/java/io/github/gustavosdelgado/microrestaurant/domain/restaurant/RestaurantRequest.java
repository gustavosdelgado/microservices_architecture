package io.github.gustavosdelgado.microrestaurant.domain.restaurant;

import lombok.NonNull;

public record RestaurantRequest(String id, @NonNull String name) {

}
