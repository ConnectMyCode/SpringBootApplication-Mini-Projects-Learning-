package org.spring.core.learn.spring14fitclubmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.spring.core.learn.spring14fitclubmvc.dto.response.BookingResponse;
import org.spring.core.learn.spring14fitclubmvc.service.ClassBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final ClassBookingService classBookingService;

    public BookingController(ClassBookingService classBookingService) {
        this.classBookingService = classBookingService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> myBookings(HttpSession session){
        Long memberId = (Long) session.getAttribute("loggedInMemberId");

        if(memberId == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Not logger in");
        }

        List<BookingResponse> bookings = classBookingService.getMemberBookings(memberId)
                .stream()
                .map(BookingResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookings);
    }



}

