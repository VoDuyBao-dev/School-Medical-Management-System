package com.medical.schoolMedical.controller.parent;

import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.entities.HealthCheckRecord;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.service.HealthCheckRecordService;
import com.medical.schoolMedical.service.HealthCheckScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/parent/healthCheckRecord")
public class HealthCheckRecordParentController {
    @Autowired
    HealthCheckRecordService healthCheckRecordService;

    @GetMapping
    public String getHealthCheckRecordParent(Model model
                                             , RedirectAttributes redirectAttributes
                                            ,@RequestParam(value = "idRecord", required = false ) Long idRecord){
        if(idRecord == null){
            redirectAttributes.addFlashAttribute("error","Vui lòng chọn bản ghi khám sức khỏe phù hợp để xem chi tiết.");
            return "redirect:/parent/notification/HealthCheckRecords";
        }

        try{
            HealthCheckRecordDTO healthCheckRecordDTO = healthCheckRecordService.getCheckRecord_updateViewed(idRecord);
            model.addAttribute("healthCheckRecord", healthCheckRecordDTO);
//            log.info("healthCheckRecord in getHealthCheckRecordParent: {}", healthCheckRecordDTO);
            return "parent/healthCheckRecordParent";
        }catch (BusinessException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/parent/notification/HealthCheckRecords";
        }


    }
}
