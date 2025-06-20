package com.medical.schoolMedical.controller.user;


import com.medical.schoolMedical.entities.*;
import com.medical.schoolMedical.enums.Role;
import com.medical.schoolMedical.repositories.AdminRepository;
import com.medical.schoolMedical.repositories.ManagerRepository;
import com.medical.schoolMedical.repositories.ParentRepositoty;
import com.medical.schoolMedical.repositories.SchoolNurseRepository;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private UserService userService;


    //Trang thông tin chung
    @GetMapping({"/admin/profile", "/parent/profile", "/manager/profile", "/nurse/profile"})
    public String viewProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              Model model) {

        String username = customUserDetails.getUser().getUsername();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        Role role = user.getRole();

        String rolePath = role.name().toLowerCase();

        String homeUrl = switch (role) {
            case ADMIN -> "/admin/dashboard";
            case PARENT -> "/parent/parent-home";
            case MANAGER -> "/manager/manager-home";
            case SCHOOL_NURSE -> "/schoolnurse/nurse-home";
        };
        model.addAttribute("homeUrl", homeUrl);


        switch (role) {
            case ADMIN -> {
                Admin admin = userService.findAdminByUsername(username);
                model.addAttribute("info", admin);
            }
            case PARENT -> {
                Parent parent = userService.findParentByUsername(username);
                model.addAttribute("info", parent);
            }
            case MANAGER -> {
                Manager manager = userService.findManagerByUsername(username);
                model.addAttribute("info", manager);
            }
            case SCHOOL_NURSE -> {
                SchoolNurse nurse = userService.findNurseByUsername(username);
                model.addAttribute("info", nurse);
            }
        }

        return "profile/profile"; // Giao diện chung
    }


    //trang thông tin admin
    /*@GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        Admin admin = userService.findAdminByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("admin", admin);

        return "admin/profile";
    }*/

    @GetMapping({"/admin/edit-profile", "/parent/edit-profile", "/manager/edit-profile", "/nurse/edit-profile"})
    public String showEditProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  Model model) {
        User user = customUserDetails.getUser();
        model.addAttribute("user", user);

        switch (user.getRole()) {
            case ADMIN -> model.addAttribute("info", userService.findAdminByUsername(user.getUsername()));
            case PARENT -> model.addAttribute("info", userService.findParentByUsername(user.getUsername()));
            case MANAGER -> model.addAttribute("info", userService.findManagerByUsername(user.getUsername()));
            case SCHOOL_NURSE -> model.addAttribute("info", userService.findNurseByUsername(user.getUsername()));
        }

        return "profile/edit-profile";
    }


    /*@GetMapping("/edit-profile")
    public String showEditProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Admin admin = userService.findAdminByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("admin", admin);
        return "admin/edit-profile";
    }*/


    /*@PostMapping("/admin/update-profile")
    public String updateProfile(@ModelAttribute Admin admin,
                                @RequestParam("userId") int userId,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userId); // hoặc findByUsername
        admin.setUser(user); // Gán lại user cho admin

        userService.saveAdmin(admin);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
        return "redirect:/admin/profile";
    }*/

    //Cập nhật thông tin vào database
    @PostMapping({"/admin/update-profile", "/parent/update-profile", "/manager/update-profile", "/nurse/update-profile"})
    public String updateProfile(@RequestParam long userId,
                                @ModelAttribute("info") Object info,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                RedirectAttributes redirectAttributes) {

        Role role = customUserDetails.getUser().getRole();

        User user = userService.findById(userId);

        switch (role) {
            case ADMIN -> {
                Admin admin = (Admin) info;
                admin.setUser(user);
                userService.saveAdmin(admin);
            }
            case PARENT -> {
                Parent parent = (Parent) info;
                parent.setUser(user);
                userService.saveParent(parent);
            }
            case MANAGER -> {
                Manager manager = (Manager) info;
                manager.setUser(user);
                userService.saveManager(manager);
            }
            case SCHOOL_NURSE -> {
                SchoolNurse nurse = (SchoolNurse) info;
                nurse.setUser(user);
                userService.saveNurse(nurse);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        return "redirect:/" + role.name().toLowerCase() + "/profile";
    }
}
