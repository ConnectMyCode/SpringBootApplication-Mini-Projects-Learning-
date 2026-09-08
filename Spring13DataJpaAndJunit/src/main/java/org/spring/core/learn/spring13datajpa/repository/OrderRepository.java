package org.spring.core.learn.spring13datajpa.repository;

import org.spring.core.learn.spring13datajpa.entity.Order;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * This repository intentionally demonstrates all THREE ways of writing custom
 * queries covered in the notes, so you can compare them side by side.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    // ------------------------------------------------------------------
    // APPROACH 1: Query creation by method name
    // Spring derives the SQL purely from the method signature.
    // ------------------------------------------------------------------

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByAmountGreaterThan(Double amount);

    List<Order> findByCustomerId(Long CustomerId);

    
    // ------------------------------------------------------------------
    // APPROACH 2: Query creation by @Query annotation (JPQL, DB-independent)
    // Notice: entity property names (o.sal-equivalent) not column names,
    // and "Order" (entity class) not "orders" (table name).
    // ------------------------------------------------------------------

    @Query(value = "select o from Order o where o.amount > :amount and o.status = :status")
    List<Order> fetchOrdersByAmountAndStatus(@Param("amount") Double amount, @Param("status") OrderStatus status);


    // ------------------------------------------------------------------
    // APPROACH 3: @NamedQuery / @NamedNativeQuery
    // The query text itself lives on the Order entity (see Order.java).
    // Here we just reference it by the name we gave it: "Order.fetchByAmountAndStatus"
    // Spring Data JPA automatically wires this up because the name matches
    // "EntityClassName.methodName" convention when a method here is called
    // fetchByAmountAndStatus - but to be explicit we keep the same method name.
    // ------------------------------------------------------------------
    List<Order> fetchByAmountAndStatus(@Param("amount") Double amount, @Param("status") OrderStatus status);



    // Spring Data JPA sees this name and automatically matches it
    // to the @NamedNativeQuery named "Order.fetchAllOrdersNative"
    List<Order> fetchAllOrdersNative();

}