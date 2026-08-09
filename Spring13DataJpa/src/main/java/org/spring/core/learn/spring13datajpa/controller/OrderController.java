package org.spring.core.learn.spring13datajpa.controller;

import org.spring.core.learn.spring13datajpa.dto.CustomerOrderSummary;
import org.spring.core.learn.spring13datajpa.entity.Order;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;
import org.spring.core.learn.spring13datajpa.repository.OrderRepository;
import org.spring.core.learn.spring13datajpa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;


    // Place a new order for a customer -> triggers cascade save via OrderService
    @PostMapping("/customer/{customerId}")
    public Order placeOrder(@PathVariable Long customerId, @RequestBody Order order) {
        return orderService.placeOrder(customerId, order);
    }

    // APPROACH 1 demo: method-name derived query
    @GetMapping("/status/{status}")
    public List<Order> getByStatus(@PathVariable OrderStatus status) {
        return orderRepository.findByStatus(status);
    }


    // APPROACH 2 demo: @Query JPQL
    @GetMapping("/filter")
    public List<Order> filterOrders(@RequestParam Double amount, @RequestParam OrderStatus status) {
        return orderRepository.fetchOrdersByAmountAndStatus(amount, status);
    }

    // APPROACH 3 demo: @NamedQuery
    @GetMapping("/named-query")
    public List<Order> namedQueryDemo(@RequestParam Double amount, @RequestParam OrderStatus status) {
        return orderRepository.fetchByAmountAndStatus(amount, status);
    }

    // Multithreaded + Stream-powered report across ALL customers
    @GetMapping("/reports/summary")
    public List<CustomerOrderSummary> getFullReport() {
        return orderService.generateReportForAllCustomersConcurrently();
    }
}
