package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.enums.Role;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class SignupController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

   /* @GetMapping("/register")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "admin/signup";
    }
    @PostMapping("/register")
    public String signup(@ModelAttribute ("user") @Valid UserDTO user,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {

       if(bindingResult.hasErrors()){
            return "admin/signup";
       }
//       kiem tra dữ liệu đầu vào
       try{
           userService.validateUserInput(user);
       }catch(BusinessException ex){
           bindingResult.rejectValue("password", null, ex.getMessage());

           return "admin/signup";
       }
//       Lưu user
        try {
            userService.signUp(user);
            redirectAttributes.addFlashAttribute ("signup_user","Đăng kí thành công");
            return "redirect:/admin/index";
        }catch(BusinessException ex){
            bindingResult.rejectValue("password", null, ex.getMessage());
            return "admin/signup";
        }

    }*/
   @GetMapping("/add-user")
   public String showAddUserForm(Model model) {
       model.addAttribute("user", new User());
       model.addAttribute("roles", Role.values());
       return "admin/add-user";
   }


    /*@PostMapping("/add-user")
    public String addUser(@ModelAttribute("newUser") User user, RedirectAttributes redirectAttributes ) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // mã hóa mật khẩu
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Thêm người dùng thành công!");
        return "redirect:/admin/manage-users";
    }*/

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute ("user") @Valid UserDTO user,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            return "admin/add-user";
        }
//       kiem tra dữ liệu đầu vào
        try{
            userService.validateUserInput(user);
        }catch(BusinessException ex){
            bindingResult.rejectValue("password", null, ex.getMessage());

            return "admin/add-user";
        }
//       Lưu user
        try {
            userService.signUp(user);
            redirectAttributes.addFlashAttribute ("success","Đăng kí người dùng thành công");
            return "redirect:/admin/manage-users";
        }catch(BusinessException ex){
            bindingResult.rejectValue("password", null, ex.getMessage());
            return "admin/add-user";
        }

    }
}
