/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmingtechie.orderService.service;

import com.programmingtechie.orderService.dto.InventoryResponse;
import com.programmingtechie.orderService.dto.OrderLineItemsDto;
import com.programmingtechie.orderService.dto.OrderRequest;
import com.programmingtechie.orderService.model.Order;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.programmingtechie.orderService.model.OrderLineItems;
import com.programmingtechie.orderService.repository.OrderRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    
    public String placeOrder(OrderRequest orderRequest) {
        Order order= new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);
        
        List<String> skuCodes= order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        
        InventoryResponse[] inventoryResponseArray= webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder-> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        
        boolean allProductsInStock= Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);
        
        if(allProductsInStock) {
            orderRepository.save(order);
            return "Order placed successfully";
        }
        else throw new IllegalArgumentException("Product is not in stock, please try again later");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
