package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.repositories.HealthCheckConsentRepository;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/schoolNurse/healthCheckConsent")
public class HealthCheckConsentController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;
    @Autowired
    private HealthCheckConsentRepository healthCheckConsentRepository;

    @GetMapping
    public String healthCheckConsent(Model model){
        model.addAttribute("healthCheckConsent",new HealthCheckConsentDTO());
        return "admin/healthCheckConsent";
    }

    @PostMapping("/send")
    public String send_HealthCheck(@ModelAttribute("healthCheckConsent") @Valid HealthCheckConsentDTO healthCheckConsentDTO
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , Model model){
        if(bindingResult.hasErrors()){
            return "admin/healthCheckConsent";
        }

        try{
            boolean create_consent = healthCheckConsentService.create_checkConsent(healthCheckConsentDTO);
            redirectAttributes.addFlashAttribute("create_consent", "Tạo health check consent thành công");
            return "redirect:/nurse/nurse-home";
        }catch (BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "admin/healthCheckConsent";
        }

    }
//    Danh sách các lịch khám sức khỏe đã gửi
    @GetMapping("/list-health-check")
    public String healthCheckConsent_list(Model model){
        List<HealthCheckConsentDTO> healthChecks = healthCheckConsentService.getAllHealthCheckConsents();
        model.addAttribute("sentSchedules",healthChecks);
        return  "admin/listSentSchedules";
    }


//    Lấy danh sách các học sinh đc phụ huynh đồng ý cho khám sức khỏe và đã được khám
    @GetMapping("/list-student-health-check/checked-health")
    public String listStudent_checkedHealth(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkDate,
                                          @RequestParam(defaultValue = "0") int page,
                                          Model model){
        boolean is_checked_health = true;
        Page<StudentDTO> consentPage  = healthCheckConsentService.getStudentsHealthCheck(checkDate, page,is_checked_health);

        model.addAttribute("consentPage", consentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consentPage.getTotalPages());
        model.addAttribute("checkDate", checkDate);

        return  "admin/danhSachStuden_daKhamSucKhoe";
    }

    //    Lấy danh sách các học sinh đc phụ huynh đồng ý cho khám sức khỏe nhưng chưa khám
    @GetMapping("/list-student-health-check")
    public String listStudent_healthCheck(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkDate,
                                          @RequestParam(defaultValue = "0") int page,
                                          Model model){
        boolean is_checked_health = false;
        Page<StudentDTO> consentPage  = healthCheckConsentService.getStudentsHealthCheck(checkDate, page,is_checked_health);

        model.addAttribute("consentPage", consentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consentPage.getTotalPages());
        model.addAttribute("checkDate", checkDate);

        return  "admin/listStudentHealthCheck";
    }


}
