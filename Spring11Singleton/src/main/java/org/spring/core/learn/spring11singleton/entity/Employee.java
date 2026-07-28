package org.spring.core.learn.spring11singleton.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namel;
    private String departement;
    private double salary;


    public Employee(Long id ,String namel, String departement, double salary) {
       this.id = id;
        this.namel = namel;
        this.departement = departement;
        this.salary = salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNamel(String namel) {
        this.namel = namel;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
