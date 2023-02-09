package io.github.gustavosdelgado.microrestaurant.domain.restaurant;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequest {

    private String id;
    
    @NonNull
    private String name;

}
