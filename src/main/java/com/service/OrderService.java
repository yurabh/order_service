package com.service;

import com.dto.OrderDto;

import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<String> saveOrder(OrderDto orderDto);
}
