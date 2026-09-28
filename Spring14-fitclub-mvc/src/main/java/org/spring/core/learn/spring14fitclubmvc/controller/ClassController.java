package org.spring.core.learn.spring14fitclubmvc.controller;


import jakarta.servlet.http.HttpSession;
import org.spring.core.learn.spring14fitclubmvc.dto.response.FitnessClassResponse;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.spring.core.learn.spring14fitclubmvc.service.ClassBookingService;
import org.spring.core.learn.spring14fitclubmvc.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * ================= REST API: replaces old DashboardController =================
 * There's no single "dashboard page" anymore - React composes the UI out
 * of API calls like GET /api/classes below, then decides layout itself.
 */
@RestController
@RequestMapping("/api/classes")
public class ClassController {


    private final ClassBookingService classBookingService;
    private final MemberService memberService;


    public ClassController(ClassBookingService classBookingService, MemberService memberService) {
        this.classBookingService = classBookingService;
        this.memberService = memberService;
    }



    /*
     * GET /api/classes
     * Combines the class itself (nested trainers + schedule), its live
     * confirmed-booking count (Collectors.groupingBy report), and
     * whether the current logged-in member favorited it - one call gives
     * React everything needed to render a full class card.
     */
    @GetMapping
    public ResponseEntity<List<FitnessClassResponse>> listClasses(HttpSession session) {
        Long memberId = requireLoggedIn(session);

        List<FitnessClass> classes = classBookingService.listAllClasses();
        Map<String, Long> bookingCounts = classBookingService.getConfirmedBookingCountByClassName();


        Set<Long> favoriteIds = memberId == null
                ? Set.of()
                : memberService.getFavoriteClasses(memberId).stream()
                .map(FitnessClass::getId)
                .collect(Collectors.toSet());

        List<FitnessClassResponse> response = classes.stream()
                .map(fc -> FitnessClassResponse.from(
                        fc,
                        bookingCounts.getOrDefault(fc.getClassName(), 0L),
                        favoriteIds.contains(fc.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{classId}/book")
    public ResponseEntity<Void> bookClass(@PathVariable Long classId, HttpSession session) {
        Long memberId = requireLoggedIn(session);
        classBookingService.bookClass(memberId, classId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    // toggles ON - idempotent, thanks to Set semantics
    @PostMapping("/{classId}/favorite")
    public ResponseEntity<Void> favoriteClass(@PathVariable Long classId, HttpSession session)
    {
        Long memberId = requireLoggedIn(session);
        memberService.addFavoriteClass(memberId,classId);
        return ResponseEntity.noContent().build();
    }


    // toggles OFF
    @DeleteMapping("/{classId}/favorite")
    public ResponseEntity<Void> unfavoriteClass(@PathVariable Long classId, HttpSession session) {
        Long memberId = requireLoggedIn(session);
        memberService.removeFavoriteClass(memberId, classId);
        return ResponseEntity.noContent().build();
    }




    private Long requireLoggedIn(HttpSession session) {
        Long memberId = (Long) session.getAttribute("loggedInMemberId");
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return memberId;
    }





}
