package com.medical.schoolMedical.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/manager-home")
    public String managerHome(Model model, Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : "Guest";// Lấy tên người đăng nhập
        model.addAttribute("username", username);
        return "manager/manager-home"; // Trả về view: templates/manager
    }
}
