package org.spring.core.learn.spring14fitclubmvc.dto.response;


import org.spring.core.learn.spring14fitclubmvc.entity.Trainer;

public class TrainerResponse {
    private final Long id;
    private final String name;
    private final String specialization;

    public TrainerResponse(Long id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public static TrainerResponse from(Trainer trainer) {
        return new TrainerResponse(trainer.getId(), trainer.getName(), trainer.getSpecialization());
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
}
