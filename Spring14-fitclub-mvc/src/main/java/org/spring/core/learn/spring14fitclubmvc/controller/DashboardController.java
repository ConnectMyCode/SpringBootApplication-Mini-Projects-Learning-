package org.spring.core.learn.spring14fitclubmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.spring.core.learn.spring14fitclubmvc.service.ClassBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboards")
public class DashboardController {

private final ClassBookingService classBookingService;


    public DashboardController(ClassBookingService classBookingService) {
        this.classBookingService = classBookingService;
    }


    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        // Guard clause: if there's no logged-in member in the session,
        // send them back to login instead of showing the dashboard.
        Object memberId = session.getAttribute("loggedInMemberId");
        if (memberId == null) {
            return "redirect:/login";
        }
        System.out.println(session);
        model.addAttribute("username", session.getAttribute("loggedInUsername"));
        model.addAttribute("classes", classBookingService.listAllClasses());
        // Stream-based report (Collectors.groupingBy) surfaced right on the dashboard
        model.addAttribute("bookingCountByClass", classBookingService.getConfirmedBookingCountByClassName());
        return model.toString();
    }

}




