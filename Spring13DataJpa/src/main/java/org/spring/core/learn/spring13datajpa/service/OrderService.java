package org.spring.core.learn.spring13datajpa.service;


import org.spring.core.learn.spring13datajpa.dto.CustomerOrderSummary;
import org.spring.core.learn.spring13datajpa.entity.Customer;
import org.spring.core.learn.spring13datajpa.entity.Order;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;
import org.spring.core.learn.spring13datajpa.repository.CustomerRepository;
import org.spring.core.learn.spring13datajpa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * REAL-WORLD SCENARIO: Think of this like a nightly batch job at an e-commerce
 * company that generates an "order summary report" for every customer
 * (total spend, order counts by status). With thousands of customers, doing this
 * one-by-one sequentially is slow, so we compute each customer's summary
 * concurrently using a thread pool - a common pattern in real backend systems.
 */
@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Adds a new order to an existing customer and saves it via cascading save
     * (CascadeType.ALL on Customer.orders means we don't need a separate
     * orderRepository.save() call here).
     */
    public Order placeOrder(Long customerId, Order newOrder) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found: " + customerId));

        // CONCEPT: Collections Framework -> adding to a List
        customer.getOrders().add(newOrder);
        customerRepository.save(customer); // cascades and saves the new Order too

        // return the last order we just added (now has a generated id)
        return (Order) customer.getOrders().get(customer.getOrders().size() - 1);
    }

    /**
     * Builds a report for ONE customer using the Stream API over their orders.
     * This is pure business logic - no SQL, we already have the objects in memory.
     */
    public CustomerOrderSummary buildSummaryFor(Customer customer) {
        List<Order> orders = orderRepository.findByCustomerId(customer.getId());

        // CONCEPT: Stream API - sum all order amounts (like SQL's SUM aggregate,
        // but done in Java over the in-memory collection)
        double totalSpent = orders.stream()
                .mapToDouble(Order::getAmount)
                .sum();

        // CONCEPT: Stream API + Collectors.groupingBy -> group orders by status and
        // count how many fall into each group. Produces a Map<OrderStatus, Long>.
        Map<OrderStatus, Long> countByStatus = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

        return new CustomerOrderSummary(
                customer.getId(),
                customer.getName(),
                orders.size(),
                totalSpent,
                countByStatus
        );
    }

    /**
     * CONCEPT: Multithreading (ExecutorService + Future/CompletableFuture).
     *
     * Instead of looping through every customer sequentially, we submit one task
     * per customer to a fixed thread pool, so multiple summaries are computed
     * in parallel - mirroring how a real reporting/batch service would scale.
     */
    public List<CustomerOrderSummary> generateReportForAllCustomersConcurrently() {
        List<Customer> allCustomers = customerRepository.findAll();

        // Thread pool sized to available CPU cores - a sensible default for
        // CPU-light, I/O-touching tasks like this.
        int poolSize = Math.min(8, Math.max(2, Runtime.getRuntime().availableProcessors()));
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        try {
            // CONCEPT: Collections Framework - build a List<Future<...>>, one per customer
            List<Future<CustomerOrderSummary>> futures = allCustomers.stream()
                    .map(customer -> executor.submit(() -> buildSummaryFor(customer)))
                    .collect(Collectors.toList());

            // Now collect the results back - each future.get() blocks only until
            // that particular thread's task is done.
            return futures.stream()
                    .map(this::resolveFuture)
                    .collect(Collectors.toList());
        } finally {
            // Always shut down the pool so we don't leak threads
            executor.shutdown();
        }
    }

    private CustomerOrderSummary resolveFuture(Future<CustomerOrderSummary> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to compute customer summary", e);
        }
    }

    /**
     * Small helper exception so this file has zero external dependencies beyond JDK/Spring.
     */
    static class NoSuchElementException extends RuntimeException {
        NoSuchElementException(String message) {
            super(message);
        }
    }
}
