package org.spring.core.learn.spring13datajpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;




/**
 * CONCEPT: @NamedQuery - defined once at the entity level, referenced by name in the
 * repository. Precompiled by the JPA provider -> better performance + one source of truth.
 * Naming convention followed: "EntityClassName.methodName" (per notes).
 *
 * CONCEPT: @NamedNativeQuery - same idea, but the query is raw SQL instead of JPQL.
 */

@NamedQuery(
        name= "Order.fetchByAmountAndStatus",
        query = "select o from Order o where o.status = :status and o.amount > :amount"
)
@NamedNativeQuery(
        name = "Order.fetchAllOrdersNative",
        query = "select * from orders",
        resultClass = Order.class
    )


@Entity
@Table(name = "value")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {



    /**
     * CONCEPT: GenerationType.IDENTITY - relies on the DB's own auto-increment column.
     * Simplest strategy, id is assigned by the DB the moment the row is inserted.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;



    @Column(name = "amount" , nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable=false)
    private OrderStatus status;

    @Column(name="order_date")
    private LocalDate orderDate;


    /**
     * Foreign key column. Owned/managed from the Customer side via @JoinColumn,
     * but we still map it here (insertable=false, updatable=false) purely so we
     * can read which customer an order belongs to without needing a @ManyToOne
     * mapping (that section wasn't covered in our notes yet).
     */
    @Column(name = "customer_id", insertable = false , updatable = false)
    private Long customerId;
}