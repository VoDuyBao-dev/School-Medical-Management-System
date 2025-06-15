package com.medical.schoolMedical.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String admin(Model model, Authentication authentication) {
        String username = authentication.getName();     //lấy tên đăng nhaapj của admin
        model.addAttribute("username", username);
        return "admin/dashboard";
    }

    @GetMapping("/manage-users")
    public String manageUsers(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "admin/manage-users";
    }


}
