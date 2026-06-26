package com.example.Sale_Savvy.Controller;

import com.example.Sale_Savvy.Entities.Order;
import com.example.Sale_Savvy.Entities.OrderStatus;
import com.example.Sale_Savvy.Entities.User;
import com.example.Sale_Savvy.Repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://sale-savvy-frontend.vercel.app"
        },
        allowCredentials = "true"
)
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @GetMapping("/myorders")
    public List<Order> getMyOrders(HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }

        return orderRepository.findByUserIdAndStatus(
                user.getUserId(),
                OrderStatus.SUCCESS
        );
    }


    @GetMapping("/count")
    public long getOrderCount(HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }

        return orderRepository.findByUserIdAndStatus(
                user.getUserId(),
                OrderStatus.SUCCESS
        ).size();
    }

}