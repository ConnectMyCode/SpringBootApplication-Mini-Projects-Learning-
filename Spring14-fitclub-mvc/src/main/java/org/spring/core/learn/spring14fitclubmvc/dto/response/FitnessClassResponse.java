package org.spring.core.learn.spring14fitclubmvc.dto.response;


import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Nests TrainerResponse and ScheduleResponse - lets the React frontend
 * get everything needed to render a class card (trainers + schedule +
 * booking count) in a SINGLE API call.
 */
public class FitnessClassResponse {

    private final Long id;
    private final String className;
    private final String description;
    private final int capacity;
    private final long confirmedBookingCount;
    private final boolean favorited;
    private final ScheduleResponse schedule;
    private final List<TrainerResponse> trainers;

    public FitnessClassResponse(Long id, String className, String description, int capacity,
                                long confirmedBookingCount, boolean favorited,
                                ScheduleResponse schedule, List<TrainerResponse> trainers) {
        this.id = id;
        this.className = className;
        this.description = description;
        this.capacity = capacity;
        this.confirmedBookingCount = confirmedBookingCount;
        this.favorited = favorited;
        this.schedule = schedule;
        this.trainers = trainers;
    }

    public static FitnessClassResponse from(FitnessClass fc, long confirmedBookingCount, boolean favorited) {
        List<TrainerResponse> trainerResponses = fc.getTrainers().stream()
                .map(TrainerResponse::from)
                .collect(Collectors.toList());
        return new FitnessClassResponse(fc.getId(), fc.getClassName(), fc.getDescription(), fc.getCapacity(),
                confirmedBookingCount, favorited, ScheduleResponse.from(fc.getSchedule()), trainerResponses);
    }

    public Long getId() { return id; }
    public String getClassName() { return className; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public long getConfirmedBookingCount() { return confirmedBookingCount; }
    public boolean isFavorited() { return favorited; }
    public ScheduleResponse getSchedule() { return schedule; }
    public List<TrainerResponse> getTrainers() { return trainers; }
}

