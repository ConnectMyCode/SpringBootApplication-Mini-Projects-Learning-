package org.spring.core.learn.spring14fitclubmvc.dto.response;



import org.spring.core.learn.spring14fitclubmvc.entity.ClassSchedule;

import java.time.LocalTime;

public class ScheduleResponse {
    private final String dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public ScheduleResponse(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ScheduleResponse from(ClassSchedule schedule) {
        if (schedule == null) return null;
        return new ScheduleResponse(schedule.getDayOfWeek(), schedule.getStartTime(), schedule.getEndTime());
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}
