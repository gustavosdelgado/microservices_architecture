package io.github.gustavosdelgado.microgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
class UriConfiguration {

    private final String restaurantUri = "http://restaurant:8081";

    private final String orderUri = "http://order:8181";

    private final String authUri = "http://auth:9000";

    public String getRestaurantUri() {
        return restaurantUri;
    }

    public String getOrderUri() {
        return orderUri;
    }

    public String getAuthUri() {
        return authUri;
    }

}
