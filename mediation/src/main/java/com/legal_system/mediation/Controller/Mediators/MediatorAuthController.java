package com.legal_system.mediation.Controller.Mediators;

import com.legal_system.mediation.Service.MediatorsService;
import com.legal_system.mediation.model.Mediators;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mediator")
public class MediatorAuthController {

    @Autowired
    private MediatorsService mediatorsService;

    // Show registration page
    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        if (!model.containsAttribute("mediator")) {
            model.addAttribute("mediator", new Mediators());
        }
        return "mediator_register";
    }

    @PostMapping("/sign-up")
    public String registerMediator(@ModelAttribute("mediator") Mediators mediator,
                                   Model model) {
        try {
            mediatorsService.addMediators(mediator);
            model.addAttribute("success", "Registration successful! Please sign in.");
            return "redirect:/mediator/sign-in";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mediator", mediator);
            return "mediator_register";
        }
    }

    // Show login page
    @GetMapping("/sign-in")
    public String showLoginForm() {
        return "mediator_login";
    }

    // Handle login - UPDATED to store in session
    @PostMapping("/sign-in")
    public String loginMediator(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        Mediators mediator = mediatorsService.authenticate(email, password);

        if (mediator != null) {
            // Store mediator ID in session
            session.setAttribute("loggedInMediatorId", mediator.getId());
            return "redirect:/mediator/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "mediator_login";
        }
    }
}