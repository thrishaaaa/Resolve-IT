package com.legal_system.mediation.Controller.Admin;

import com.legal_system.mediation.Service.UserService;
import com.legal_system.mediation.model.UserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public String signIn(@RequestParam String email,
                         @RequestParam String password,
                         HttpSession session,
                         Model model) {

        Optional<UserDetails> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            UserDetails user = userOptional.get();
            if (user.getPassword().trim().equals(password.trim())) {
                // store logged-in user in session
                session.setAttribute("loggedInUserId", user.getId());
                return "redirect:/resolve-it-through-mediation";
            }
        }

        model.addAttribute("error", "Invalid email or password");
        return "sign-in";
    }


    @GetMapping("/sign-in")
    public String showSignInPage() {
        return "login";
    }
}
