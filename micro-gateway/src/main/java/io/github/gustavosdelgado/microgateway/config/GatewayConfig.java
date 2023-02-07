package io.github.gustavosdelgado.microgateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UriConfiguration.class)
public class GatewayConfig {

    @Autowired
    private JwtAuthFilter filter;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
        String httpbin = uriConfiguration.getHttpbin();
        return builder.routes()
                .route(p -> p
                        .path("/auth")
                        .filters(f -> f.filter(filter))
                        .uri(httpbin + "/authenticate"))
                .build();
    }

}
