package io.github.gustavosdelgado.microgateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.gustavosdelgado.microgateway.filter.RestaurantGatewayFilter;

@Configuration
@EnableConfigurationProperties(UriConfiguration.class)
public class RoutesConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
        return builder.routes()
				.route("restaurant_command", p -> p
						.path("/restaurant/**")
						.filters(f -> f.rewritePath("/restaurant/(?<segment>.*)", "/restaurant/${segment}")
								.circuitBreaker(config -> config
										.setName("restaurant_cb")))
						.uri(uriConfiguration.getRestaurantUri()))
				.route("restaurant_query", p -> p
						.path("/restaurant")
						.filters(f -> f.filter(new RestaurantGatewayFilter())
								.circuitBreaker(config -> config
										.setName("restaurant_cb")))
						.uri(uriConfiguration.getRestaurantUri()))
				.route("order_command", p -> p
						.path("/order/**")
						.filters(f -> f.rewritePath("/order/(?<segment>.*)", "/order/${segment}")
								.circuitBreaker(config -> config
										.setName("order_cb")))
						.uri(uriConfiguration.getOrderUri()))
				.route("order_query", p -> p
						.path("/order")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("order_cb")))
						.uri(uriConfiguration.getOrderUri()))
				.route("auth", p -> p
						.path("/authenticate")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("auth_cb")))
						.uri(uriConfiguration.getAuthUri()))
				.route("user", p -> p
						.path("/user")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("auth_cb")))
						.uri(uriConfiguration.getAuthUri()))
                .build();
    }

}
