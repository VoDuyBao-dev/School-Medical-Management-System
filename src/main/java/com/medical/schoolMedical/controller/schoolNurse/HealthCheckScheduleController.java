package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.dto.VaccinationScheduleDTO;
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

//     tạo lịch
    @PostMapping("/createSchedule")
    public String send_HealthCheck(@ModelAttribute("healthCheckSchedule") HealthCheckScheduleDTO healthCheckScheduleDTO
            , @AuthenticationPrincipal CustomUserDetails customUserDetails
            , RedirectAttributes redirectAttributes
            , Model model){

        LocalDate date = LocalDate.parse(healthCheckScheduleDTO.getDate());
        LocalTime time = LocalTime.parse(healthCheckScheduleDTO.getTime());
        LocalDateTime checkDateTime = LocalDateTime.of(date, time);

//        Lấy id cuar người dùng hiện tại (nurse)
        Long userID = customUserDetails.getUser().getId();

        healthCheckScheduleDTO.setCheckDate(checkDateTime);
//        log.info(healthCheckScheduleDTO.toString());

        try{
            HealthCheckScheduleDTO result = healthCheckScheduleService.create_checkSchedule(healthCheckScheduleDTO, userID);
            redirectAttributes.addFlashAttribute("create_checkSchedule", "Tạo health check schedule thành công");
            return "redirect:/nurse/nurse-home";

        }catch (BusinessException e){
           model.addAttribute("error",e.getMessage());
            return "admin/healthCheckConsent";
        }

    }

    //    Danh sách các lịch khám sức khỏe đã tạo nhưng chưa gửi
    @GetMapping("/healthCheckSchedules/drafts")
    public String healthCheckScheduleDrafts_list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<HealthCheckScheduleDTO> healthCheckSchedules = healthCheckScheduleService.getAllHealthCheckSchedule_drafts(page);

        log.info("healthCheckSchedules in healthCheckScheduleDrafts_list: {}", healthCheckSchedules.getContent());

        model.addAttribute("draftsHealthCheckSchedules",healthCheckSchedules.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", healthCheckSchedules.getTotalPages());
        return  "nurse/healthCheck-schedule-list";
    }

    //    Danh sách các lịch khám sức khỏe đã gửi
    @GetMapping("/healthCheckSchedules/sent")
    public String healthCheckScheduleSent_list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<HealthCheckScheduleDTO> healthChecks = healthCheckScheduleService.getAllHealthCheckSchedule_sent(page);
        model.addAttribute("sentSchedules",healthChecks.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", healthChecks.getTotalPages());
        return  "admin/listSentSchedules";
    }
//
    //    Gửi lịch khám sức khỏe đến phụ huynh
    @PostMapping("/sentToParent")
    public String sentHealthCheckSchedule(@ModelAttribute("healthCheckScheduleId") Long healthCheckScheduleId
            , RedirectAttributes redirectAttributes
            , Model model) {

        HealthCheckScheduleDTO healthCheckScheduleDTO = null;
        try{
            healthCheckScheduleDTO = healthCheckScheduleService.getHealthCheckScheduleById(healthCheckScheduleId);
            log.info("healthCheckScheduleDTO in sentHealthCheckSchedule: {}", healthCheckScheduleDTO);

        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/nurse/nurse-home";
        }

        try{
//                    Gửi lịch đến phụ huynh:
            healthCheckConsentService.sendCheckSchedule_toParent(healthCheckScheduleDTO);
            redirectAttributes.addFlashAttribute("success", "Gửi lịch khám sức khỏe đến parent thành công");
            return "redirect:/nurse/nurse-home";
        }catch (BusinessException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/nurse/nurse-home";
        }

    }


}
