package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.enums.Role;
import com.medical.schoolMedical.repositories.AdminRepository;
import com.medical.schoolMedical.service.StatisticsService;
import com.medical.schoolMedical.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatisticsService statisticsService;


    @GetMapping({"/dashboard", "/"})
    public String admin(Model model, Authentication authentication) {
        String username = authentication.getName();     //lấy tên đăng nhaapj của admin
        model.addAttribute("username", username);
        return "admin/dashboard";
    }

    @GetMapping("/manage-users")
    public String manageUsers(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        List<User> users = userService.findAllUsers();  // lấy danh sách user chưa bị xóa
        model.addAttribute("users", users);

        model.addAttribute("newUser", new User()); // dùng cho form thêm mới
        model.addAttribute("roles", Role.values()); // danh sách role để chọn trong form
        return "admin/manage-users";
    }


    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable int id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("editUser", user);
        model.addAttribute("roles", Role.values());
        return "admin/edit-user";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("editUser") User user, RedirectAttributes redirectAttributes) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Nếu không đổi mật khẩu thì giữ nguyên
            User existing = userService.findById(user.getId());
            user.setPassword(existing.getPassword());
        }
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Cập nhật người dùng thành công!");
        return "redirect:/admin/manage-users";
    }



    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.softDeleteUser(id);
        return "redirect:/admin/manage-users";
    }

    //        Tính toán và thống kê số liệu
    @GetMapping("/chart")
    public String showChart(Model model, @RequestParam(defaultValue = "2025") int year) {
        List<Integer> years = List.of(2023, 2024, 2025);
        List<Integer> vaccinationCounts = statisticsService.getMonthlyVaccinationCounts(year);
        log.info("vaccinationCounts in showChart {}:" , vaccinationCounts);
        List<Integer> healthCheckCounts = statisticsService.getMonthlyHealthCheckCounts(year);

        model.addAttribute("years", years);
        model.addAttribute("selectedYear", year);
        model.addAttribute("vaccinationCounts", vaccinationCounts);
        model.addAttribute("healthCheckCounts", healthCheckCounts);
        return "admin/testBieuDoThongKe";
    }


}
