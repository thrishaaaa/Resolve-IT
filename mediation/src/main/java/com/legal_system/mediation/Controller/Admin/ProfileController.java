package com.legal_system.mediation.Controller.Admin;
import com.legal_system.mediation.Service.ProfileService;
import com.legal_system.mediation.model.UserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String viewProfile(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        if (userId == null) return "redirect:/sign-in";

        UserDetails user = profileService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfilePage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        if (userId == null) return "redirect:/sign-in";

        UserDetails user = profileService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile_edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute UserDetails userDetails,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("loggedInUserId");
            if (userId == null) return "redirect:/sign-in";

            userDetails.setId(userId);
            profileService.updateProfile(userDetails);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam String confirmation,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("loggedInUserId");
            if (userId == null) return "redirect:/sign-in";

            if (!"DELETE".equals(confirmation)) {
                redirectAttributes.addFlashAttribute("error", "Please type DELETE to confirm");
                return "redirect:/profile";
            }

            profileService.deleteAccount(userId);
            session.invalidate();
            return "redirect:/sign-up?deleted=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete account: " + e.getMessage());
            return "redirect:/profile";
        }
    }

}
