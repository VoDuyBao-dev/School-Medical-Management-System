package com.medical.schoolMedical.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Trangchutamthoi {
    @GetMapping("/admin/index")
    public String index(Model model){
        return "admin/trangchuTamThoi";
    }
}
