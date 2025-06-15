package com.medical.schoolMedical.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/parent")
public class ParentController {

    @GetMapping("/parent-home")
    public String parentHome(Model model, Authentication authentication) {
        String username = authentication.getName(); // Lấy tên người dùng đang đăng nhập
        model.addAttribute("username", username);
        return "parent/parent-home"; // Trả về view: templates/parent/home.html
    }

}
