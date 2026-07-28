package org.spring.core.learn.spring11singleton.repository;

import org.spring.core.learn.spring11singleton.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartement(String departement);
}



