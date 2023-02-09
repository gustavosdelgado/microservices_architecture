package io.github.gustavosdelgado.microrestaurant.domain.restaurant;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class RestaurantResponse {

    private Long id;

    private String name;

    private String user;
    
}
