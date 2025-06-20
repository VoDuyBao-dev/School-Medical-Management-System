package com.medical.schoolMedical.controller.user;

import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Slf4j
@Controller
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    HealthCheckConsentService healthCheckConsentService;


    //    Nếu hiện thông báo rồi thì đánh dấu nó để k hiện lại nhiều lần
    @PostMapping("/notification/dismiss")
    @ResponseBody
    public void dismissNotification(HttpSession session) {
        session.setAttribute("notificationSeen", true);
    }


    @GetMapping("/parent-home")
    public String parentHome(Model model, Authentication authentication, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             HttpSession session) {
        String username = authentication.getName(); // Lấy tên người dùng đang đăng nhập
        model.addAttribute("username", username);
        Long loggedInUserID = customUserDetails.getUser().getId();
        log.info("loggedInUserID : {}", loggedInUserID);

//        Kiểm tra xem có phiếu kiểm tra sức khỏe nào mới không
        boolean hasNewHealthCheckNotification = healthCheckConsentService.hasNewNotification(loggedInUserID);
        log.info("hasNewHealthCheckNotification : {}", hasNewHealthCheckNotification);

//        Kiểm tra nếu đã hiện thông báo rồi thì không hiện trong phiên đó nx
        Boolean notificationSeen = (Boolean) session.getAttribute("notificationSeen");
        // Nếu chưa từng thấy trong session này
        boolean notSeenYet  = notificationSeen == null || !notificationSeen;
//Chỉ hiện nếu có phiếu mới và Chưa xem trong phiên
        boolean showNotification = hasNewHealthCheckNotification && notSeenYet;

        model.addAttribute("showNotification", showNotification);
        return "parent/parent-home"; // Trả về view: templates/parent/home.html
    }

}
