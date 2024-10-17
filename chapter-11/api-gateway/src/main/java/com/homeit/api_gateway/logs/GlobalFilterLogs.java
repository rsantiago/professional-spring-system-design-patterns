package com.homeit.api_gateway.logs;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GlobalFilterLogs implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalFilterLogs.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
      // Log the request path and headers
      logger.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
      exchange.getRequest().getHeaders().forEach((name, values) -> {
        values.forEach(value -> logger.debug("Request Header: {}={}", name, value));
      });

      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        // Log the response status code and headers
        logger.info("Outgoing response: {}", exchange.getResponse().getStatusCode());
        exchange.getResponse().getHeaders().forEach((name, values) -> {
          values.forEach(value -> logger.debug("Response Header: {}={}", name, value));
        });
      }));
    }

    @Override
    public int getOrder() {
      return -1; // Ensure this filter is applied first
    }
}