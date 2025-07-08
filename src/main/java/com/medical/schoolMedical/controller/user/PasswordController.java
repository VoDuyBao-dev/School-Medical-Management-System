package com.medical.schoolMedical.controller.user;

import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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


//    Quên password
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {

        return "user/forgotpass";
    }

//    Xác thực 2 pass mới và xác thực đã giống chưa ở quên password
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("userID") long userId,Model model) {
        model.addAttribute("userID", userId);
        return "user/newpass";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("userID") long userId,
                                @RequestParam("password") String newPassword,
                                @RequestParam("confirm-password") String confirmNewPassword,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("userID", userId);
            return "user/newpass";
        }
//        reset mật khẩu
        try{
            userService.resetPassword(userId, newPassword);
        }catch (BusinessException e){
            model.addAttribute("error", e.getMessage());
            return "user/newpass";
        }

        redirectAttributes.addFlashAttribute("success", "Đặt lại mật khẩu thành công!");
        return "redirect:/login";
    }

}
