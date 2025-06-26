package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.dto.VaccinationRecordDTO;
import com.medical.schoolMedical.entities.VaccinationRecord;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.VaccinationRecordService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/nurse/vaccinationRecord")
public class VaccinationRecordController {
    @Autowired
    VaccinationRecordService vaccinationRecordService;

    @GetMapping("/form")
    public String vaccinationRecord(@RequestParam(value = "vaccinatonScheduleId", required = false) Long idSchedule,
                                    @RequestParam(value = "vaccinatonConsentId", required = false) Long consentId,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if(idSchedule == null || consentId == null){
            redirectAttributes.addFlashAttribute("error", "id học sinh hoặc id đồng ý tiêm chủng không được để trống.");
            return "redirect:/nurse/vaccinationSchedule/vaccinationSchedules";
        }

       VaccinationRecordDTO vaccinationRecordDTO = new VaccinationRecordDTO();
        model.addAttribute("vaccinationRecordDTO",vaccinationRecordDTO);
        model.addAttribute("vaccinatonConsentId",consentId);
        model.addAttribute("vaccinatonScheduleId",idSchedule);
        return "nurse/vaccinationRecordNurse";
    }

    //    Hàm kiểm tra xem đang tạo record hay update và thực hiện gọi hàm tương ứng
    @PostMapping("/save-update-vaccinationRecord")
    public String controllerAction_vaccinationRecord(@ModelAttribute("vaccinationRecordDTO") @Valid VaccinationRecordDTO vaccinationRecordDTO,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "vaccinatonConsentId") Long consentId,
                                   @RequestParam(value = "vaccinatonScheduleId") Long idSchedule,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   RedirectAttributes redirectAttributes,
                                   Model model)
    {
        log.info("consentId in controllerAction_vaccinationRecord: {}", consentId);
        if(bindingResult.hasErrors()){
            model.addAttribute("vaccinatonConsentId", consentId);
            model.addAttribute("vaccinatonScheduleId", idSchedule);
            return "nurse/vaccinationRecordNurse";
        }

        log.info("vaccinationRecordDTO của /save-update-vaccinationRecord: {}", vaccinationRecordDTO);

        if(vaccinationRecordDTO.getId() == 0){


        }
        return saveVaccinationRecord(vaccinationRecordDTO,
                consentId,
                idSchedule,
                customUserDetails,
                redirectAttributes,
                model);
//        else{
//            log.info("chạy cái update");
//            return updateHealthCheckRecord(healthCheckRecordDTO,
//                    consentId,
//                    idSchedule,
//                    customUserDetails,
//                    redirectAttributes,
//                    model);
//        }

    }
    //
    public String saveVaccinationRecord(VaccinationRecordDTO vaccinationRecordDTO,
                                        Long consentId,
                                        Long idSchedule,
                                        CustomUserDetails customUserDetails,
                                        RedirectAttributes redirectAttributes,
                                        Model model)
    {
//        Lấy id của người tạo form
        Long userId = customUserDetails.getUser().getId();
        log.info("userId ==> "+userId);

//        Tạo record
        try{
            vaccinationRecordService.create_VaccinationRecord(vaccinationRecordDTO,consentId, userId);
            redirectAttributes.addFlashAttribute("success","Kết quả sau tiêm chủng đã được ghi nhận thành công!");
            return "redirect:/nurse/vaccinationConsent/list-student-vaccination?vaccinatonScheduleId=" + idSchedule;
        }catch (BusinessException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "/nurse/vaccinationConsent/list-student-vaccination?vaccinatonScheduleId=" + idSchedule;
        }

    }
}
