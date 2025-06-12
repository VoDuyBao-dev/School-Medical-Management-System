package com.medical.schoolMedical.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("user", "hello day la bao");
        return "user/index";
    }

}
