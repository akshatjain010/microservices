/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmingtechie.productService.service;

import com.programmingtechie.productService.dto.ProductRequest;
import com.programmingtechie.productService.dto.ProductResponse;
import com.programmingtechie.productService.model.Product;
import com.programmingtechie.productService.repository.ProductRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;
    private final Tracer tracer;
    
    public void createProduct(ProductRequest productRequest) {
        Span span = tracer.nextSpan().name("createProduct").start();
        try(Tracer.SpanInScope ws= tracer.withSpan(span)) {
            Product product= Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        
            productRepository.save(product);
            log.info("Product {} is saved", product.getId());
        }
        finally {
            span.end();
        }
        
    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products= productRepository.findAll();
        
//        products.stream().map(product-> mapToProductResponse(product))
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
