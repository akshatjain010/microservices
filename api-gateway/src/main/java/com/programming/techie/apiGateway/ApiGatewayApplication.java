package com.programming.techie.apiGateway;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiGatewayApplication {

    private final Tracer tracer;

    public void addTracingInfo() {
        if (tracer.currentSpan() != null) {
            MDC.put("traceId", tracer.currentSpan().context().traceId());
            MDC.put("spanId", tracer.currentSpan().context().spanId());
        }
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        Span newSpan = tracer.nextSpan().name("custom-span").start();
        try(Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan)) {
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
        finally {
            newSpan.end();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
