package io.github.gustavosdelgado.microgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
class UriConfiguration {

    private String restaurantUri = "http://restaurant:8081";

    public String getRestaurantUri() {
        return restaurantUri;
    }

    public void setRestaurantUri(String restaurantUri) {
        this.restaurantUri = restaurantUri;
    }
}
