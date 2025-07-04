package com.medical.schoolMedical.controller.user;

import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping({"/admin/change-password", "/parent/change-password", "/manager/change-password", "/nurse/change-password"})
    public String changePasswordPage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     Model model) {
        User user = customUserDetails.getUser();
        model.addAttribute("username", customUserDetails.getUser().getUsername());
        model.addAttribute("role", customUserDetails.getUser().getRole().name().toLowerCase());
        return "profile/change-password"; // Dùng chung file HTM
    }


    @PostMapping({"/admin/change-password", "/parent/change-password", "/manager/change-password", "/nurse/change-password"})
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 RedirectAttributes redirectAttributes) {

        User user = customUserDetails.getUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
            return "redirect:/" + user.getRole().name().toLowerCase() + "/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/" + user.getRole().name().toLowerCase() + "/change-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/" + user.getRole().name().toLowerCase() + "/profile";
    }


}
