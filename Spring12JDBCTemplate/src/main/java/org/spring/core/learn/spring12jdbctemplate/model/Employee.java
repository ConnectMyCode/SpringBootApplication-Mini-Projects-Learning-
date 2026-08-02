package org.spring.core.learn.spring12jdbctemplate.model;


/**
 * Plain Java object (POJO) that represents one row of the EMP table.
 * RowMapper<Employee> (in EmployeeRepository) knows how to convert
 * a ResultSet row into an object of this class.
 *
 * 'bonus' is NOT a database column -> it is calculated later at runtime
 * by calling the bonus_function() stored function via SimpleJdbcCall,
 * and then set on this object in the service layer. This mirrors a very
 * common real-world pattern: DB gives you raw data, business layer enriches it.
 */
public class Employee {

    private int empno;
    private String ename;
    private double sal;
    private String department;

    // Not persisted in DB -> filled in by PayrollService after calling the DB function
    private double bonus;

    public Employee() {
    }

    public Employee(int empno, String ename, double sal, String department) {
        this.empno = empno;
        this.ename = ename;
        this.sal = sal;
        this.department = department;
    }

    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public double getSal() {
        return sal;
    }

    public void setSal(double sal) {
        this.sal = sal;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    // Total take-home = salary + bonus. Used a lot in the Streams logic later.
    public double getTotalPay() {
        return sal + bonus;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empno=" + empno +
                ", ename='" + ename + '\'' +
                ", sal=" + sal +
                ", department='" + department + '\'' +
                ", bonus=" + bonus +
                ", totalPay=" + getTotalPay() +
                '}';
    }
}
