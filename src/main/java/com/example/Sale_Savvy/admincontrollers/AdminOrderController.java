package com.example.Sale_Savvy.admincontrollers;

import com.example.Sale_Savvy.Entities.Order;
import com.example.Sale_Savvy.Entities.OrderStatus;
import com.example.Sale_Savvy.Repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderRepository orderRepository;

    public AdminOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // View all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // View only pending orders
    @GetMapping("/pending")
    public List<Order> getPendingOrders() {
        return orderRepository.findPendingOrders();
    }


    // View successful orders
    @GetMapping("/success")
    public List<Order> getSuccessfulOrders()
    {
        return orderRepository.findSuccessfulOrders();
    }

    @PutMapping("/status")
    public Order updateOrderStatus(@RequestBody Map<String, String> requestBody)
    {
        String orderId = requestBody.get("orderId");
        OrderStatus status = OrderStatus.valueOf(requestBody.get("status"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}