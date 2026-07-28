package org.spring.core.learn.spring11singleton.controller;

import org.spring.core.learn.spring11singleton.cache.EmployeeCache;
import org.spring.core.learn.spring11singleton.entity.Employee;
import org.spring.core.learn.spring11singleton.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repo;

    public EmployeeController(EmployeeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Employee> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        // Try cache first (singleton in action)
        Employee cached = EmployeeCache.getInstance().get(id);
        if (cached != null) return cached;

        Employee emp = repo.findById(id).orElseThrow();
        EmployeeCache.getInstance().put(emp);
        return emp;
    }
}