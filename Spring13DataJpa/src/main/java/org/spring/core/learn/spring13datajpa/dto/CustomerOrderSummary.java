package org.spring.core.learn.spring13datajpa.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;

import java.util.Map;


/**
 * Plain DTO (Data Transfer Object) - not a JPA entity, just a container we build
 * ourselves in the service layer to send a computed report back to the client.
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderSummary {

    private Long customerId;
    private String customerName;
    private long totalOrders;
    private double totalAmountSpent;

    // CONCEPT: Collections Framework - Map is used to hold "status -> count of orders"
    // e.g. { "DELIVERED": 3, "PENDING": 1 }
    private Map<OrderStatus, Long> orderCountByStatus;}