/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmingtechie.productService.controller;

import com.programmingtechie.productService.dto.ProductRequest;
import com.programmingtechie.productService.dto.ProductResponse;
import com.programmingtechie.productService.service.ProductService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    
    private static Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final Tracer tracer;
    
    public void addTracingInfo() {
        if (tracer.currentSpan() != null) {
            MDC.put("traceId", tracer.currentSpan().context().traceId());
            MDC.put("spanId", tracer.currentSpan().context().spanId());
        }
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        Span newSpan = tracer.nextSpan().name("custom-span").start();
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan)) {
            productService.createProduct(productRequest);
        }
        finally {
            newSpan.end();
        }
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProduct();
    }
}
