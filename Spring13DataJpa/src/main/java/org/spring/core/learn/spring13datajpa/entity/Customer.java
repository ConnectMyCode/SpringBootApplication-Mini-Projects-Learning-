package org.spring.core.learn.spring13datajpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * CONCEPT: @Entity marks this class as a JPA entity -> Hibernate will map it to a table.
 * CONCEPT: @Table is technically optional here (class name "Customer" ~ table name),
 *          but we specify it explicitly for clarity, as good practice in real projects.
 */
@Entity
@Table(name = "customers")
@Data                   // Lombok: generates getters, setters, toString, equals/hashCode
@NoArgsConstructor      // Lombok: JPA REQUIRES a no-args constructor on every entity
@AllArgsConstructor     // Lombok: convenient constructor for tests/demo data
public class Customer {

    /**
     * CONCEPT: GenerationType.SEQUENCE - id values come from a DB sequence object.
     * allocationSize=5 means Hibernate reserves 5 ids in memory at a time,
     * reducing round-trips to the DB (per notes).
     */


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_id_sequence", allocationSize = 5)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "name" , nullable = false)
    private  String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;



    /**
     * CONCEPT: one-to-many relationship (parent -> children).
     * We deliberately use the UNIDIRECTIONAL style shown in the notes:
     *   @OneToMany + @JoinColumn directly on the parent (no @ManyToOne needed on Order).
     *
     * cascade = CascadeType.ALL  -> save/delete on Customer cascades to its Orders. If customer is deleted then Customers order are also deleted
     * fetch   = FetchType.LAZY   -> orders are NOT loaded until we actually access them
     *                               (this is also the JPA default for @OneToMany).

     * @JoinColumn tells Hibernate the foreign key column ("customer_id") lives on the
     * "orders" table and points back to  -Customer's primary key ("customer_id").
     */

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private List<Order> orders = new ArrayList<>();
}


