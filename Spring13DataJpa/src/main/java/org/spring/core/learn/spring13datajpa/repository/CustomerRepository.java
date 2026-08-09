package org.spring.core.learn.spring13datajpa.repository;

import org.spring.core.learn.spring13datajpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CONCEPT: JpaRepository<T, ID> gives us save(), findById(), findAll(), deleteById(),
 * saveAndFlush(), deleteAllInBatch() etc for free - no implementation needed,
 * Spring generates it at runtime.
 */

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * CONCEPT: Query creation by method name.
     * Spring parses "findByNameContainingIgnoreCase" and builds:
     *   SELECT * FROM customers WHERE UPPER(name) LIKE UPPER('%pattern%')
     */

    List<Customer> findByNameContainingIgnoreCase(String namePattern);

}
