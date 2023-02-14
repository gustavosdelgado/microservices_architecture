package io.github.gustavosdelgado.microgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
class UriConfiguration {

    private String restaurantUri = "http://restaurant:8081";

    private String orderUri = "http://order:8181";

    public String getRestaurantUri() {
        return restaurantUri;
    }

    public String getOrderUri() {
        return orderUri;
    }

}
