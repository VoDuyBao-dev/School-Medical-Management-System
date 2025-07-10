package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.enums.Role;
import com.medical.schoolMedical.repositories.AdminRepository;
import com.medical.schoolMedical.service.StatisticsService;
import com.medical.schoolMedical.service.StudentService;
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
import java.util.Map;

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

    @Autowired
    private StudentService studentService;


    @GetMapping({"/dashboard", "/"})
    public String admin(Model model, Authentication authentication) {
        String username = authentication.getName();     //lấy tên đăng nhaapj của admin
        model.addAttribute("username", username);

//        Tinhs toán thống kê

        List<Integer> vaccinationCounts = statisticsService.getMonthlyVaccinationCounts(2025);
        List<Integer> healthCheckCounts = statisticsService.getMonthlyHealthCheckCounts(2025);
        List<Integer> medicalEventCounts = statisticsService.getMonthlyMedicalEventCounts(2025);

        log.info("vaccinationCounts in showChart {}:" , vaccinationCounts);
        log.info("healthCheckCounts in showChart {}:" , healthCheckCounts);
        log.info("medicalEventCounts in showChart {}:" , medicalEventCounts);



//        data biểu đồ healthCheck và Vaccination
        model.addAttribute("vaccinationCounts", vaccinationCounts);
        model.addAttribute("healthCheckCounts", healthCheckCounts);
//        Biểu đồ medicalEventCounts
        model.addAttribute("medicalEventCounts", medicalEventCounts);

//        thống kê số lượng phụ huynh, hs, nurse ... tronbg hệ thống
        Map<String, Long> counts = statisticsService.getUserCountsByRole();
        model.addAttribute("counts", counts);

        List<StudentDTO> studentsThisMonth = studentService.getStudentsCreatedThisMonth();
        List<StudentDTO> studentsLastMonth = studentService.getStudentsCreatedLastMonth();
        log.info("studentsLastMonth in showChart {}:" , studentsLastMonth);
        log.info("studentsThisMonth in showChart {}:" , studentsThisMonth);

        model.addAttribute("studentsLastMonth", studentsLastMonth);
        model.addAttribute("studentsThisMonth", studentsThisMonth);

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
//    @GetMapping("/chart")
//    public String showChart(Model model, @RequestParam(defaultValue = "2025") int year) {
//
//        return "admin/dashboard";
//    }


}
