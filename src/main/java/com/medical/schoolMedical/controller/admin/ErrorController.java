package com.medical.schoolMedical.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    //không đủ quyền truy cập sẽ chuyeenr trang
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "admin/access-denied";
    }
}
