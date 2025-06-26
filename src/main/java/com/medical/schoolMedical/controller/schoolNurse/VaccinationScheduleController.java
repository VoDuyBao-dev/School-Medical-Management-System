package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.dto.VaccinationScheduleDTO;
import com.medical.schoolMedical.entities.VaccinationSchedule;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.VaccinationConsentService;
import com.medical.schoolMedical.service.VaccinationScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/nurse/vaccinationSchedule")
public class VaccinationScheduleController {

    @Autowired
    private VaccinationScheduleService vaccinationScheduleService;
    @Autowired
    private VaccinationConsentService vaccinationConsentService;

    @GetMapping
    public String vaccinationSchedule(Model model) {
        model.addAttribute("vaccinationSchedule", new VaccinationScheduleDTO());
        return "nurse/vaccinationScheduleNurse";

    }

//    Tạo và gửi lịch tiêm đến phụ huynh
    @PostMapping("/create")
    public String createVaccinationSchedule(@ModelAttribute("vaccinationSchedule") @Valid VaccinationScheduleDTO vaccinationScheduleDTO
                                            , BindingResult bindingResult
                                            ,@AuthenticationPrincipal CustomUserDetails customUserDetails
                                            , RedirectAttributes redirectAttributes
                                            , Model model) {
        log.info("Creating vaccination schedule: {}", vaccinationScheduleDTO);
        if (bindingResult.hasErrors()) {
            return "nurse/vaccinationScheduleNurse";
        }
        Long userId = customUserDetails.getUser().getId();
        try{
            VaccinationScheduleDTO result = vaccinationScheduleService.createVaccinationSchedule(vaccinationScheduleDTO, userId);
            redirectAttributes.addFlashAttribute("success", "Tạo lịch tiêm chủng thành công");

//            gửi đến phụ huynh:
            try{
//                    Gửi lịch đến phụ huynh:
                vaccinationConsentService.sendVaccinationSchedule_toParent(result);
                redirectAttributes.addFlashAttribute("success", "Gửi health check schedule đến parent thành công");
                return "redirect:/nurse/nurse-home";
            }catch (BusinessException e){
                redirectAttributes.addFlashAttribute("error",e.getMessage());
                return "redirect:/nurse/nurse-home";
            }

        }catch (BusinessException e){
            redirectAttributes.addFlashAttribute("error", "Tạo lịch tiêm chủng thất bại");
            return "redirect:/nurse/nurse-home";
        }

    }

    //    Danh sách các lịch tiêm chủng đã gửi
    @GetMapping("/vaccinationSchedules")
    public String vaccinationSchedule_list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<VaccinationScheduleDTO> vaccinationSchedules = vaccinationScheduleService.getAllVaccinationSchedule(page);
        model.addAttribute("sentVaccinationSchedules",vaccinationSchedules.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vaccinationSchedules.getTotalPages());
        return  "nurse/ListSentVaccinationSchedules";
    }
}
