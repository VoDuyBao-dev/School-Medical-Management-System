package com.medical.schoolMedical.controller.parent;

import com.medical.schoolMedical.dto.ConsultationAppointmentDTO;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.ConsultationAppointmentService;
import com.medical.schoolMedical.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/parent/consultationAppointment")
public class ConsultationAppointmentParentController {
    @Autowired
    StudentService studentService;
    @Autowired
    ConsultationAppointmentService consultationAppointmentService;

    @GetMapping("/listReview")
    public String listParentReview(Model model, @RequestParam(defaultValue = "0") int page
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        Lấy id user của phụ huynh
        long userId = customUserDetails.getUser().getId();
        Page<ConsultationAppointmentDTO> appointmentDTOS = consultationAppointmentService.getAllAppointmentAccepted_Parent(userId,page);
        model.addAttribute("appointmentDTOS",appointmentDTOS.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appointmentDTOS.getTotalPages());
        return "parent/listReview";
    }



    @GetMapping("/confirmReview")
    public String confirmReview() {
        return "parent/confirmReview";
    }
}
