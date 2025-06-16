package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {
    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "user/signup";
    }
    @PostMapping("/register")
    public String signup(@ModelAttribute ("user") @Valid UserDTO user,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {

       if(bindingResult.hasErrors()){
            return "user/signup";
       }
//       kiem tra dữ liệu đầu vào
       try{
           userService.validateUserInput(user);
       }catch(BusinessException ex){
           bindingResult.rejectValue("password", null, ex.getMessage());

           return "user/signup";
       }
//       Lưu user
        try {
            userService.signUp(user);
            redirectAttributes.addFlashAttribute ("signup_user","Đăng kí thành công");
            return "redirect:/login";
        }catch(BusinessException ex){
            bindingResult.rejectValue("password", null, ex.getMessage());
            return "user/signup";
        }

    }
}
