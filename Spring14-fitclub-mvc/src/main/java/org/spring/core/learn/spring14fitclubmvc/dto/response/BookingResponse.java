package org.spring.core.learn.spring14fitclubmvc.dto.response;

import org.spring.core.learn.spring14fitclubmvc.entity.Booking;

import java.time.LocalDateTime;

public class BookingResponse {

    private final Long id;
    private final String className;
    private final LocalDateTime bookingDate;
    private final String status;

    public BookingResponse(Long id, String className, LocalDateTime bookingDate, String status) {
        this.id = id;
        this.className = className;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public static BookingResponse from(Booking booking){

        return new BookingResponse(
                booking.getId(),
                booking.getFitnessClass().getClassName(),
                booking.getBookingDate(),
                booking.getStatus().name());
    }


    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }}