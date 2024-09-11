package com.zimzim.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private final RequestFilter requestFilter;
    private final AuthFilter authFilter;

    public GatewayConfig(RequestFilter requestFilter, AuthFilter authFilter) {
        this.requestFilter = requestFilter;
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("first-microservice", r -> r.path("/first")
                        .and().method("POST")
                        .and().readBody(Student.class, s -> true)
                        .filters(f -> f.filters(requestFilter, authFilter))
                        .uri("http://localhost:8081"))
                .route("first-microservice", r -> r.path("/first")
                        .and().method("GET")
                        .filters(f -> f.filters(authFilter))
                        .uri("http://localhost:8081"))
                .route("second-microservice", r -> r.path("/second")
                        .and().method("POST")
                        .and().readBody(Company.class, s -> true)
                        .filters(f -> f.filters(requestFilter, authFilter))
                        .uri("http://localhost:8082"))
                .route("second-microservice", r -> r.path("/second")
                        .and().method("GET")
                        .filters(f -> f.filters(authFilter))
                        .uri("http://localhost:8082"))
                .route("auth-server", r -> r.path("/login")
                        .uri("http://localhost:8088"))
                .build();
    }
}
