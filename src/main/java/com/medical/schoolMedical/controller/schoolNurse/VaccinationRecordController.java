package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.dto.VaccinationRecordDTO;
import com.medical.schoolMedical.entities.VaccinationRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/nurse/vaccinationRecord")
public class VaccinationRecordController {

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
        model.addAttribute("consentId",consentId);
        model.addAttribute("idSchedule",idSchedule);
        return "nurse/vaccinationRecordNurse";
    }

}
