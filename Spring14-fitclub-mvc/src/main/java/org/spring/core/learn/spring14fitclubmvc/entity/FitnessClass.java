package org.spring.core.learn.spring14fitclubmvc.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fitness_classes")
public class FitnessClass {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String className;

    @Column(length = 500)
    private String description;

    // Business rule limit - checked in ClassBookingServiceImpl against a
    // COUNT query on Booking, not against an in-memory collection size.
    @Column(nullable = false)
    private int capacity;




    /*
     * ================= RELATIONSHIP 4 (continued): @OneToOne OWNING SIDE, UNIDIRECTIONAL =================
     * @JoinColumn here means FitnessClass owns this relationship (the
     * fitness_classes table gets a schedule_id foreign key column).
     * "unique = true" is what makes this a true one-to-one instead of
     * silently behaving like a many-to-one - the same rule as
     * MemberProfile's @JoinColumn.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", unique= true)
    private ClassSchedule schedule;




    /*
     * ================= RELATIONSHIP 5 (continued): @ManyToMany OWNING SIDE, BIDIRECTIONAL =================
     * @JoinTable is declared HERE, making FitnessClass the owning side -
     * this is what actually creates the join table
     * "fitness_class_trainers" with two foreign key columns (class_id,
     * trainer_id). Trainer.java has the mappedBy="trainers" inverse side.
     */
    @ManyToMany
    @JoinTable(
            name = "fitness_class_trainers",
            joinColumns= @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    @JsonIgnore
    private Set<Trainer> trainers = new HashSet<>();



    public FitnessClass() {}

    public FitnessClass(Long id, String className, String description, int capacity) {
        this.id = id;
        this.className = className;
        this.description = description;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public ClassSchedule getSchedule() { return schedule; }
    public void setSchedule(ClassSchedule schedule) { this.schedule = schedule; }

    public Set<Trainer> getTrainers() { return trainers; }
    public void setTrainers(Set<Trainer> trainers) { this.trainers = trainers; }



}
