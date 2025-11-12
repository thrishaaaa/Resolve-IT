package com.legal_system.mediation.Controller.Mediators;

import com.legal_system.mediation.Service.MediatorDashboardService;
import com.legal_system.mediation.model.Cases;
import com.legal_system.mediation.model.MeetingSchedule;
import com.legal_system.mediation.model.Mediators;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/mediator")
public class MediatorDashboardController {

    @Autowired
    private MediatorDashboardService dashboardService;

    // Dashboard home page
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Integer mediatorId = (Integer) session.getAttribute("loggedInMediatorId");

        if (mediatorId == null) {
            return "redirect:/mediator/sign-in";
        }

        // Get mediator details
        Mediators mediator = dashboardService.getMediatorById(mediatorId);
        if (mediator == null) {
            return "redirect:/mediator/sign-in";
        }

        // Get statistics
        Long totalCases = dashboardService.getTotalCases(mediatorId);
        Long activeCases = dashboardService.getActiveCases(mediatorId);
        Long resolvedCases = dashboardService.getResolvedCases(mediatorId);
        Long upcomingMeetings = dashboardService.getUpcomingMeetingsCount(mediatorId);

        // Get cases and meetings
        List<Cases> cases = dashboardService.getMediatorCases(mediatorId);
        List<MeetingSchedule> meetings = dashboardService.getUpcomingMeetings(mediatorId);

        // Add to model
        model.addAttribute("mediator", mediator);
        model.addAttribute("totalCases", totalCases);
        model.addAttribute("activeCases", activeCases);
        model.addAttribute("resolvedCases", resolvedCases);
        model.addAttribute("upcomingMeetings", upcomingMeetings);
        model.addAttribute("cases", cases);
        model.addAttribute("meetings", meetings);

        return "mediator-dashboard";
    }

    // Update case status
    @PostMapping("/update-case-status")
    public String updateCaseStatus(@RequestParam Integer caseId,
                                   @RequestParam String status,
                                   HttpSession session) {
        Integer mediatorId = (Integer) session.getAttribute("loggedInMediatorId");
        if (mediatorId == null) {
            return "redirect:/mediator/sign-in";
        }

        dashboardService.updateCaseStatus(caseId, status);
        return "redirect:/mediator/dashboard";
    }

    // Create new meeting
    @PostMapping("/create-meeting")
    public String createMeeting(@RequestParam String date,
                                @RequestParam String time,
                                @RequestParam String description,
                                HttpSession session) {
        Integer mediatorId = (Integer) session.getAttribute("loggedInMediatorId");
        if (mediatorId == null) {
            return "redirect:/mediator/sign-in";
        }

        LocalDate meetingDate = LocalDate.parse(date);
        LocalTime meetingTime = LocalTime.parse(time);

        dashboardService.createMeeting(mediatorId, meetingDate, meetingTime, description);
        return "redirect:/mediator/dashboard";
    }

    // Cancel meeting
    @PostMapping("/cancel-meeting")
    public String cancelMeeting(@RequestParam Integer meetingId, HttpSession session) {
        Integer mediatorId = (Integer) session.getAttribute("loggedInMediatorId");
        if (mediatorId == null) {
            return "redirect:/mediator/sign-in";
        }

        dashboardService.cancelMeeting(meetingId);
        return "redirect:/mediator/dashboard";
    }
}