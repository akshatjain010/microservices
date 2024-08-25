/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmingtechie.orderService.controller;

import com.programmingtechie.orderService.dto.OrderRequest;
import com.programmingtechie.orderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final Tracer tracer;
    
    public void addTracingInfo() {
        if (tracer.currentSpan() != null) {
            MDC.put("traceId", tracer.currentSpan().context().traceId());
            MDC.put("spanId", tracer.currentSpan().context().spanId());
        }
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name= "inventory", fallbackMethod= "fallbackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        Span newSpan = tracer.nextSpan().name("custom-span").start();
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan)) {
            return CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRequest));
        }
        finally {
            newSpan.end();
        }
    }
    
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public String placeOrder(@RequestBody OrderRequest orderRequest) {
//        Span newSpan = tracer.nextSpan().name("custom-span").start();
//        try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan)) {
//            return orderService.placeOrder(orderRequest);
//        }
//        finally {
//            newSpan.end();
//        }
//    }
    
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        Span newSpan = tracer.nextSpan().name("custom-span").start();
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan)) {
            return CompletableFuture.supplyAsync(()-> "Oops! Something went wrong, please order after sometime.");
        }
        finally {
            newSpan.end();
        }
    }
}
