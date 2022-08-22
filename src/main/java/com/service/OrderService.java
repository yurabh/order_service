package com.service;

import com.client.InventoryClient;
import com.domain.Order;
import com.domain.OrderLineItems;
import com.dto.OrderDto;
import com.dto.OrderLineItemsDto;
import com.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final StreamBridge streamBridge;

    public String saveOrder(OrderDto orderDto) {
        boolean allProductsInStock = allProductsInStock(orderDto);
        if (allProductsInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            List<OrderLineItems> orderLineItems = getOrderLineItems(orderDto);
            order.setOrderLineItems(orderLineItems);
            orderRepository.save(order);
            log.info("Sending Order details to Notification Service");
            streamBridge.send("notificationEventSupplier-out-0", order.getId());
            return "Order Placed Successfully";
        }
        return "Order Failed One of the product in the order is not in stock";
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    private List<OrderLineItems> getOrderLineItems(OrderDto orderDto) {
        return orderDto.getOrderLineItems()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private boolean allProductsInStock(OrderDto orderDto) {
        return orderDto.getOrderLineItems()
                .stream()
                .allMatch(orderLineItems -> inventoryClient.checkStock(orderLineItems.getSkuCode()));
    }
}
