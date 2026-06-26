package com.example.Sale_Savvy.Repository;

import com.example.Sale_Savvy.Entities.Order;
import com.example.Sale_Savvy.Entities.OrderStatus;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // Custom query methods can be added here if needed
    @Query("SELECT o FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrdersByMonthAndYear(int month, int year);



    @Query("SELECT o FROM Order o WHERE DATE(o.createdAt) = :date AND o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrdersByDate(LocalDate date);



    @Query("SELECT o FROM Order o WHERE YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrdersByYear(int year);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status='SUCCESS'")
    List<Order> findByUserIdAndStatus(int userId, OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'SUCCESS'")
    BigDecimal calculateOverallBusiness();

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findAllByStatus(OrderStatus status);


    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'SUCCESS'")
    long countSuccessfulOrders();


    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING'")
    List<Order> findPendingOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrders();
}
