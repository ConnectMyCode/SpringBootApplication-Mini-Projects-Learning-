package org.spring.core.learn.spring12jdbctemplate.repository;

import org.spring.core.learn.spring12jdbctemplate.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * This class demonstrates EVERY JdbcTemplate method from your notes:
 *   1) execute()        -> DDL  (CREATE TABLE)
 *   2) update()         -> DML  (INSERT / DELETE)
 *   3) queryForMap()     -> single row as Map
 *   4) queryForList()    -> multiple rows as List<Map>
 *   5) queryForObject()  -> single scalar value
 *   6) query() + RowMapper<T> -> multiple rows as List<Employee> (Java objects, not Maps)
 */

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS EMP (" +
                "EMPNO  INT PRIMARY KEY, " +
                "ENAME VARCHAR(30)" +
                "SAL DOUBLE, " +
                "DEPARTMENT VARCHAR(40))";
        jdbcTemplate.execute(sql);
    }

    // ---------- 2) update() : DML - INSERT ----------
    public void saveEmployee(Employee e) {
        String sql = "INSERT IGNORE INTO EMP VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, e.getEmpno(), e.getEname(), e.getSal(), e.getDepartment());
    }

    // ---------- 2) update() : DML - DELETE ----------
    public void deleteEmployeeById(int empno) {
        String sql = "DELETE FROM EMP WHERE EMPNO= ?";
        jdbcTemplate.update(sql, empno);
    }


    // ---------- 3) queryForMap() : single row ----------
    public Map<String, Object> fetchEmployeeAsMap(int empno) {
        String sql = "SELECT * FROM EMP WHERE EMPNO = ?";
        return jdbcTemplate.queryForMap(sql, empno);
    }

    // ---------- 4) queryForList() : multiple rows as raw Maps ----------
    public List<Map<String, Object>> fetchAllAsListOfMaps() {
        String sql = "SELECT * FROM EMP";
        return jdbcTemplate.queryForList(sql);
    }

    // ---------- 5) queryForObject() : single scalar value ----------
    public double fetchAverageSalary() {
        String sql = "SELECT AVG(SAL) FROM EMP";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    // ---------- 6) query() + RowMapper<T> : rows mapped to Employee objects ----------
    public List<Employee> fetchAllEmployees() {

        // RowMapper is a functional interface -> we implement it with a lambda.
        // For every row in the ResultSet, this lambda builds one Employee object.
        RowMapper<Employee> mapper = (rs, rowNum) -> {
            Employee e = new Employee();
            e.setEmpno(rs.getInt("EMPNO"));
            e.setEname(rs.getString("ENAME"));
            e.setSal(rs.getDouble("SAL"));
            e.setDepartment(rs.getString("DEPARTMENT"));
            return e;
        };
        String sql = "SELECT * FROM EMP";
        return jdbcTemplate.query(sql, mapper);
    }
}
