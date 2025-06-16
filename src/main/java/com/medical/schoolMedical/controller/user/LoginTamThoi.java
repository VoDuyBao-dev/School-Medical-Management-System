package com.medical.schoolMedical.controller.user;

import com.fasterxml.jackson.databind.DatabindContext;
import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginTamThoi {
    @Autowired
    UserService userService;

    @GetMapping
    public String login(Model model){
        model.addAttribute("user", new UserDTO());
        return "user/loginTamThoi";
    }

    @PostMapping
    public String login(@ModelAttribute("user") UserDTO user, Model model, HttpSession session){
        log.info("data in userDTO {}", user.getUsername());
        String role;
        try{
            user = userService.login(user);
            try{
                role = userService.getRole(user);
            }catch(NullPointerException e){
                model.addAttribute("error", e.getMessage());
                return "user/loginTamThoi";
            }

            session.setAttribute("loggedInUser", user); // lưu vào session
            if(!role.equals("PARENT")){
                
                return "redirect:/admin/index";
            }
            return "redirect:/home";

        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "user/loginTamThoi";
        }



    }
}
