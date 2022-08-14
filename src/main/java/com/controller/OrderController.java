package com.controller;

import com.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.service.OrderService;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String saveOrder(@RequestBody OrderDto orderDto) {
        orderService.saveOrder(orderDto);
        return "Order Placed Successfully";
    }
}
