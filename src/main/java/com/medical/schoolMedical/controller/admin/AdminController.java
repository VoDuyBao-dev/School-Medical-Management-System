package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.enums.Role;
import com.medical.schoolMedical.repositories.AdminRepository;
import com.medical.schoolMedical.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

    /*@GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        return "admin/add-user";
    }


    @PostMapping("/add-user")
    public String addUser(@ModelAttribute("newUser") User user, RedirectAttributes redirectAttributes ) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // mã hóa mật khẩu
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Thêm người dùng thành công!");
        return "redirect:/admin/manage-users";
    }*/

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

    //trang thông tin admin
    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        Admin admin = userService.findAdminByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("admin", admin);

        return "admin/profile";
    }


    //chỉnh sửa thông tin admin
    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Admin admin = userService.findAdminByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("admin", admin);
        return "admin/edit-profile";
    }

    //cập nhat thong tin vào database
    /*@PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("admin") Admin admin, RedirectAttributes redirectAttributes) {
        userService.saveAdmin(admin);
        redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        return "redirect:/admin/edit-profile";
    }*/

    @PostMapping("/admin/update-profile")
    public String updateProfile(@ModelAttribute Admin admin,
                                @RequestParam("userId") int userId,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userId); // hoặc findByUsername
        admin.setUser(user); // Gán lại user cho admin

        userService.saveAdmin(admin);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
        return "redirect:/admin/profile";
    }



    //đổi mật khẩu dành cho admin
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "admin/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
            return "redirect:/admin/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/admin/change-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/admin/profile";
    }


}
