package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import com.medical.schoolMedical.service.HealthCheckScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@Slf4j
@RequestMapping("/nurse/healthCheckSchedule")
public class HealthCheckScheduleController {
    @Autowired
    private HealthCheckScheduleService healthCheckScheduleService;
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;

    @GetMapping
    public String healthCheckSchedule(Model model) {

        model.addAttribute("healthCheckSchedule",new HealthCheckScheduleDTO());
        return "admin/healthCheckConsent";
    }

//    Vừa tạo lịch vừa gửi đến cho phụ huynh
    @PostMapping("/createSchedule-sendParent")
    public String send_HealthCheck(@ModelAttribute("healthCheckSchedule") @Valid HealthCheckScheduleDTO healthCheckScheduleDTO
            , @AuthenticationPrincipal CustomUserDetails customUserDetails
            , @RequestParam("checkDatePart") String checkDatePart
            , @RequestParam("checkTimePart") String checkTimePart
            , RedirectAttributes redirectAttributes
            , Model model){

        LocalDate date = LocalDate.parse(checkDatePart);
        LocalTime time = LocalTime.parse(checkTimePart);
        LocalDateTime checkDateTime = LocalDateTime.of(date, time);

//        Lấy id cuar người dùng hiện tại (nurse)
        Long userID = customUserDetails.getUser().getId();

        healthCheckScheduleDTO.setCheckDate(checkDateTime);
        log.info(healthCheckScheduleDTO.toString());

        try{
            HealthCheckScheduleDTO result = healthCheckScheduleService.create_checkSchedule(healthCheckScheduleDTO, userID);

            redirectAttributes.addFlashAttribute("create_checkSchedule", "Tạo health check schedule thành công");
            try{
//                    Gửi lịch đến phụ huynh:
                healthCheckConsentService.sendCheckSchedule_toParent(result);
                redirectAttributes.addFlashAttribute("send_parent", "Gửi health check schedule đến parent thành công");
                return "redirect:/nurse/nurse-home";
            }catch (BusinessException e){
                model.addAttribute("error",e.getMessage());
                return "admin/healthCheckConsent";
            }

        }catch (BusinessException e){
           model.addAttribute("error",e.getMessage());
            return "admin/healthCheckConsent";
        }

    }

    //    Danh sách các lịch khám sức khỏe đã gửi
    @GetMapping("/list-healthCheckSchedule")
    public String healthCheckSchedule_list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<HealthCheckScheduleDTO> healthChecks = healthCheckScheduleService.getAllHealthCheckSchedule(page);
        model.addAttribute("sentSchedules",healthChecks.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", healthChecks.getTotalPages());
        return  "admin/listSentSchedules";
    }
}
