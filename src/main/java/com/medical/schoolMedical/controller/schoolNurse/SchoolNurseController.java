package com.medical.schoolMedical.controller.schoolNurse;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nurse")
public class SchoolNurseController {
    @GetMapping("/nurse-home")
    public String nurseHome(Model model, Authentication authentication) {
        String username = authentication.getName(); // Lấy tên người dùng
        model.addAttribute("username", username);
        return "nurse/nurse-home"; // templates/nurse/home.html
    }

    // Tư vấn
    @GetMapping("nurse/listReview")
    public String listReview() {
        return "nurse/listReview";
    }

    @GetMapping("parent/listReview")
    public String listParentReview() {
        return "parent/listReview";
    }

    @GetMapping("nurse/listCreatedReview")
    public String listCreatedReview() {
        return "nurse/listCreatedReview";
    }

    @GetMapping("nurse/createReview")
    public String createReview() {
        return "nurse/createReview";
    }

    @GetMapping("parent/confirmReview")
    public String confirmReview() {
        return "parent/confirmReview";
    }

}
