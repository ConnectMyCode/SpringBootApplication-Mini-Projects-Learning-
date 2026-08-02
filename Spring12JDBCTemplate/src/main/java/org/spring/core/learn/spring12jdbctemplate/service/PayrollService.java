package org.spring.core.learn.spring12jdbctemplate.service;

import org.spring.core.learn.spring12jdbctemplate.model.Employee;
import org.spring.core.learn.spring12jdbctemplate.repository.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    @Autowired
    private BonusRepository bonusRepository;


/**
 * This is the BUSINESS LOGIC layer of our mini project.
 * It is where we practice: Multithreading, Collection Framework, and Stream API
 * on top of the data we fetched from the DB using JDBC.
 *
 * Real-world parallel: a payroll batch job that has to call an external/DB
 * "bonus calculation" service for hundreds of employees. Calling it one by
 * one (sequentially) is slow -> so we fire the calls concurrently using a
 * thread pool, exactly like a real payroll microservice would.
 */



    /**
     * MULTITHREADING CONCEPT:
     * We use an ExecutorService (fixed thread pool) so multiple bonus_function()
     * DB calls happen IN PARALLEL instead of one after another.
     * Each employee's bonus calculation is submitted as a Callable<Employee> task.
     * Future<Employee> lets us collect the result once the thread finishes.
     */
    public List<Employee> enrichEmployeesWithBonus(List<Employee> employees) throws InterruptedException, ExecutionException {

        // Thread pool size kept small & fixed -> good practice, avoids creating
        // unlimited threads (which is a common beginner mistake).
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Collection Framework: List<Future<Employee>> holds one "pending result" per employee.
        List<Future<Employee>> futures = new ArrayList<>();

        for (Employee e : employees) {
            // Callable = a task that RETURNS a value (unlike Runnable which returns nothing)
            Callable<Employee> task = () -> {
                double bonus = bonusRepository.calculateBonus(e.getEmpno());
                e.setBonus(bonus);
                return e;
            };
            futures.add(executorService.submit(task));
        }

        // Collection Framework: build the final enriched list
        List<Employee> enrichedEmployees = new ArrayList<>();
        for (Future<Employee> future : futures) {
            // future.get() BLOCKS until that specific thread completes and returns its result
            enrichedEmployees.add(future.get());
        }

        // Always shut down the pool once work is done, otherwise threads keep the JVM alive
        executorService.shutdown();

        return enrichedEmployees;
    }

    /**
     * STREAM API CONCEPT: groupingBy collector
     * Groups employees by department -> Map<String, List<Employee>>
     */
    public Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    /**
     * STREAM API CONCEPT: groupingBy + downstream summingDouble collector
     * Produces total payroll cost (salary + bonus) PER department.
     */
    public Map<String, Double> departmentWiseTotalPay(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::getTotalPay)
                ));
    }

    /**
     * STREAM API CONCEPT: max() with Comparator
     * Finds the highest-paid employee (salary + bonus combined).
     */
    public Optional<Employee> findTopEarner(List<Employee> employees) {
        return employees.stream()
                .max(Comparator.comparingDouble(Employee::getTotalPay));
    }

    /**
     * STREAM API CONCEPT: sorted() + collect()
     * Returns employees sorted by total pay, highest first.
     */
    public List<Employee> sortByTotalPayDescending(List<Employee> employees) {
        return employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getTotalPay).reversed())
                .collect(Collectors.toList());
    }

    /**
     * COLLECTION FRAMEWORK CONCEPT: TreeSet
     * TreeSet automatically keeps elements sorted AND removes duplicates ->
     * perfect for "list of unique department names, alphabetically".
     */
    public Set<String> uniqueDepartmentsSorted(List<Employee> employees) {
        Set<String> departments = new TreeSet<>();
        for (Employee e : employees) {
            departments.add(e.getDepartment());
        }
        return departments;
    }



}
