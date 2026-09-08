package org.spring.core.learn.spring14fitclubmvc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(length = 60)
    private String specialization;

    @ManyToMany(mappedBy="trainers", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<FitnessClass> classes = new HashSet<>();


    public Trainer() {}

    public Trainer(Long id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Set<FitnessClass> getClasses() { return classes; }
    public void setClasses(Set<FitnessClass> classes) { this.classes = classes; }



}
