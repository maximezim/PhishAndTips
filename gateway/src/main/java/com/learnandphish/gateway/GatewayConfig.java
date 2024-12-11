package com.learnandphish.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Maxime Zimmermann
 */
@Configuration
public class GatewayConfig {
    private final AuthFilter authFilter;

    public GatewayConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/authenticate")
                        .uri("http://authentication-service:8082"))
                .route("auth-swagger", r -> r.path("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**")
                        .uri("http://authentication-service:8082"))
                .route("change-password", r -> r.path("/change-password")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://authentication-service:8082"))
                .route("export-users", r -> r.path("/export-users")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://authentication-service:8082"))
                .route("gophish", r -> r.path("/gophish/**")
                    .filters(f -> f.filter(authFilter)
                                   .rewritePath("/gophish(?<segment>/?.*)", "${segment}"))
                    .uri("http://gophish:3333"))
                // Admin Routes example, view @RouteRoles
                // .route("admin-service", r -> r.path("/admin/**")
                //         .filters(f -> f.filter(authFilter))
                //         .uri("http://admin-service:8083"))
                .route("protected-routes", r -> r.path("/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://authentication-service:8082"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); // Allow only this origin
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}