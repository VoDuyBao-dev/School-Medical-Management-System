package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.repositories.HealthCheckConsentRepository;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/nurse/healthCheckConsent")
public class HealthCheckConsentController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;
    @Autowired
    private HealthCheckConsentRepository healthCheckConsentRepository;

//    Lấy danh sách các học sinh đc phụ huynh đồng ý cho khám sức khỏe và đã được khám
    @GetMapping("/list-student-health-check/checked-health")
    public String listStudent_checkedHealth(@RequestParam(value = "idSchedule", required = false) Long idSchedule,
                                          @RequestParam(defaultValue = "0") int page,
                                          RedirectAttributes redirectAttributes,
                                          Model model){
        if(idSchedule==null){
            redirectAttributes.addFlashAttribute("error","idSchedule không được để trống");
            return "redirect:/nurse/healthCheckSchedule/list-healthCheckSchedule";
        }
        boolean is_checked_health = true;
        Page<HealthCheckConsentDTO> consentPage  = healthCheckConsentService.getStudentsHealthCheck(idSchedule, page,is_checked_health);
        log.info("consentPage={}",consentPage.getContent());

// đến đoạn giờ phải gắn mỗi cái nút là 1 id record tương ứng
        model.addAttribute("consentPage", consentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consentPage.getTotalPages());
        model.addAttribute("idSchedule", idSchedule);

        return  "admin/danhSachStuden_daKhamSucKhoe";
    }

    //    Lấy danh sách các học sinh đc phụ huynh đồng ý cho khám sức khỏe nhưng chưa khám
    @GetMapping("/list-student-health-check")
    public String listStudent_healthCheck(@RequestParam(value = "idSchedule", required = false) Long idSchedule,
                                          @RequestParam(defaultValue = "0") int page,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        if(idSchedule==null){
            redirectAttributes.addFlashAttribute("error","idSchedule không được để trống");
            return "redirect:/nurse/healthCheckSchedule/list-healthCheckSchedule";
        }
        boolean is_checked_health = false;
        Page<HealthCheckConsentDTO> consentPage  = healthCheckConsentService.getStudentsHealthCheck(idSchedule, page,is_checked_health);
        log.info("consentPage={}",consentPage.getContent());

        model.addAttribute("consentPage", consentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consentPage.getTotalPages());
        model.addAttribute("idSchedule", idSchedule);
        return  "admin/listStudentHealthCheck";
    }


}
