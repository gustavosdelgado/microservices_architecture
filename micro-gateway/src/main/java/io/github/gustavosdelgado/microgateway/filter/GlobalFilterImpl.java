package io.github.gustavosdelgado.microgateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalFilterImpl implements GlobalFilter, Ordered {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
            GatewayFilterChain chain) {
        logger.info("Global Pre Filter executed");
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    logger.info("Global Post Filter executed");
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
