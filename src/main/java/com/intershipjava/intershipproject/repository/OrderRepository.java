package com.intershipjava.intershipproject.repository;

import com.intershipjava.intershipproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order WHERE orderId = ?1")
    Order findOrderById(String orderId);

    @Query("FROM Order WHERE customerName = ?1 AND isPayed = ?2 AND customerEmail = ?3")
    Order findOrderByNameAndStatus(String name, Boolean isPaid, String email);

    @Modifying
    @Query("update Order u set u.isPayed = :status where u.orderId = :orderId")
    void updateOrderStatus(@Param(value = "orderId") String orderId, @Param(value = "status") Boolean status);
}
