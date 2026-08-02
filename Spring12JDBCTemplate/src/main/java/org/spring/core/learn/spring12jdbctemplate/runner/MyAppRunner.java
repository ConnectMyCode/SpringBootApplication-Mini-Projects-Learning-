package org.spring.core.learn.spring12jdbctemplate.runner;
import org.spring.core.learn.spring12jdbctemplate.model.Employee;
import org.spring.core.learn.spring12jdbctemplate.repository.EmployeeRepository;
import org.spring.core.learn.spring12jdbctemplate.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This is where the whole mini project executes, step by step.
 * Think of this as the "main business flow" of a payroll batch job:
 *
 *  1) Setup table + seed data          (JDBC: execute / update)
 *  2) Fetch employees from DB          (JDBC: RowMapper + query)
 *  3) Calculate bonus concurrently     (Multithreading + SimpleJdbcCall)
 *  4) Build the payroll report         (Stream API + Collections)
 */



@Component
public class MyAppRunner implements ApplicationRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollService payrollService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // ---------- Step 1: Setup ----------
        employeeRepository.createTable();

        employeeRepository.saveEmployee(new Employee(7454, "MILLER", 5000.0, "RESEARCH"));
        employeeRepository.saveEmployee(new Employee(7278, "RONALDO", 4000.0, "IT"));
        employeeRepository.saveEmployee(new Employee(7701, "WILLIAM", 7000.0, "RESEARCH"));
        employeeRepository.saveEmployee(new Employee(7328, "MARY", 4000.0, "HR"));
        employeeRepository.saveEmployee(new Employee(7788, "SCOTT", 8000.0, "IT"));

        // ---------- Step 2: Fetch from DB using RowMapper ----------
        List<Employee> employees = employeeRepository.fetchAllEmployees();
        System.out.println("\n--- Raw Employees from DB ---");
        employees.forEach(System.out::println);

        // ---------- Step 3: Enrich with bonus using MULTIPLE THREADS ----------
        List<Employee> enrichedEmployees = payrollService.enrichEmployeesWithBonus(employees);
        System.out.println("\n--- Employees after concurrent bonus calculation ---");
        enrichedEmployees.forEach(System.out::println);

        // ---------- Step 4: Business insights using STREAMS + COLLECTIONS ----------

        Map<String, List<Employee>> byDept = payrollService.groupByDepartment(enrichedEmployees);
        System.out.println("\n--- Employees grouped by Department ---");
        byDept.forEach((dept, list) -> System.out.println(dept + " -> " + list.size() + " employee(s)"));

        Map<String, Double> deptTotals = payrollService.departmentWiseTotalPay(enrichedEmployees);
        System.out.println("\n--- Department-wise Total Payroll Cost (Salary + Bonus) ---");
        deptTotals.forEach((dept, total) -> System.out.println(dept + " -> " + total));

        payrollService.findTopEarner(enrichedEmployees)
                .ifPresent(topEarner -> System.out.println("\n--- Top Earner ---\n" + topEarner));

        List<Employee> sorted = payrollService.sortByTotalPayDescending(enrichedEmployees);
        System.out.println("\n--- Employees Sorted by Total Pay (High to Low) ---");
        sorted.forEach(System.out::println);

        Set<String> departments = payrollService.uniqueDepartmentsSorted(enrichedEmployees);
        System.out.println("\n--- Unique Departments (sorted) ---");
        System.out.println(departments);

        // ---------- Bonus: use other JdbcTemplate methods too ----------
        double avgSal = employeeRepository.fetchAverageSalary();
        System.out.println("\n--- Average Salary (queryForObject) ---\n" + avgSal);

        Map<String, Object> oneEmployee = employeeRepository.fetchEmployeeAsMap(7788);
        System.out.println("\n--- Single Employee as Map (queryForMap) ---\n" + oneEmployee);
    }


}