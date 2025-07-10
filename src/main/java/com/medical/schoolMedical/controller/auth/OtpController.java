package com.medical.schoolMedical.controller.auth;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.service.EmailService;
import com.medical.schoolMedical.service.OtpService;
import com.medical.schoolMedical.service.UserService;
import com.medical.schoolMedical.util.ValidationUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/new-password")
public class OtpController {
    @Autowired
    EmailService emailService;
    @Autowired
    OtpService otpService;
    @Autowired
    UserService userService;

    // Xử lý gửi OTP
    @PostMapping
    public String sendOtp(@RequestParam String email, @RequestParam(defaultValue = "send") String action, Model model, RedirectAttributes redirectAttributes) {
        if(!ValidationUtil.isValidEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email không hợp lệ");
            return "redirect:/forgot-password";
        }
        if (!userService.existsUserByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại trong hệ thống");
            return "redirect:/forgot-password";
        }

        if ("send".equals(action)) {
                try {
                    String otp = otpService.generateOtp(email);
                    emailService.sendOtpEmail(email, otp);
                    redirectAttributes.addFlashAttribute("showOtpModal", true);
                    redirectAttributes.addFlashAttribute("email", email);
                    return "redirect:/forgot-password";
                } catch (MessagingException e) {
                    model.addAttribute("error", "Failed to send OTP: " + e.getMessage());
                    return "user/forgotpass";
                }
        }
        return "redirect:/forgot-password";
    }

    // Xử lý xác thực OTP
    @PostMapping(params = "action=verify")
    public String verifyOtp(@RequestParam String email, @RequestParam("otp[]") String[] otpArray, Model model) {
        String otp = String.join("", otpArray);
        boolean isValid = otpService.validateOtp(email, otp);
        if (isValid) {
//            Lấy id của user có email hợp lệ để cập nhật password
            UserDTO user = userService.findUserByEmail(email);
            return "redirect:/reset-password?userID=" + user.getId();
        }

        model.addAttribute("showOtpModal", true);
        model.addAttribute("email", email);
        model.addAttribute("errorOtp", "Mã OTP không hợp lệ hoặc đã hết hạn");

        return "user/forgotpass";


    }

    // Xử lý gửi lại OTP
    @PostMapping(params = "action=resend")
    public String resendOtp(@RequestParam String email, Model model) {
        try {
            String otp = otpService.generateOtp(email);
            emailService.sendOtpEmail(email, otp);
            model.addAttribute("showOtpModal", true);
            model.addAttribute("email", email);
            model.addAttribute("resendMessage", "Đã gửi lại mã OTP mới đến email của bạn");
            return "user/forgotpass";
        } catch (MessagingException e) {
            model.addAttribute("showOtpModal", true);
            model.addAttribute("email", email);
            model.addAttribute("errorOtp", "Không thể gửi mã OTP: ");
            e.printStackTrace();
            return "user/forgotpass";
        }
    }



}
