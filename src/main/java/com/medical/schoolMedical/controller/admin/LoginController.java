package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        model.addAttribute("user", new UserDTO());
        if (error != null) {
            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu!");
        }
        return "user/login";
    }

    /*@PostMapping("/login")
    public String login(@ModelAttribute("user") UserDTO user, Model model) {
        try {
            boolean isValid = userService.checkLogin(user.getUsername(), user.getPassword());

            if (isValid) {
                // Login thành công → chuyển sang /home
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng.");
                return "user/login";
            }
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "user/login";
        }
    }*/

    @GetMapping("/admin/login")
    public String adminLoginPage(Model model) {
        return "admin/login";  // View ở: templates/admin/login.html
    }
}
