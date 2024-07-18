package com.programming.techie.apiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
    
        @Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("productservice", r -> r.path("/productservice/**")
					 .filters(f -> f.stripPrefix(1))
					 .uri("lb://product-service"))
				.route("orderservice", r -> r.path("/orderservice/**")
					 .filters(f -> f.stripPrefix(1))
					 .uri("lb://order-service"))
                                .route("discoveryserver", r -> r.path("/eureka/web")
					 .filters(f -> f.setPath("/"))
					 .uri("http://localhost:8761"))
                                .route("discoveryserverstatic", r -> r.path("/eureka/**")
					 .uri("http://localhost:8761")) 
				.build();
	}
    
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
