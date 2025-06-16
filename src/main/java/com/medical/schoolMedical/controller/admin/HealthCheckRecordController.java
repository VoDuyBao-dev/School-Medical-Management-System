package com.medical.schoolMedical.controller.admin;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.HealthCheckRecord;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.helper.SessionUtil;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import com.medical.schoolMedical.service.HealthCheckRecordService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin/healthCheckRecord")
public class HealthCheckRecordController {
    @Autowired
    HealthCheckConsentService healthCheckConsentService;
    @Autowired
    HealthCheckRecordService healthCheckRecordService;
    @GetMapping("/form")
    public String healthCheckRecord(@RequestParam("stdId") Long studentID,
                                    @RequestParam("date") LocalDate date,
                                    Model model) {

        try{
            HealthCheckConsentDTO healthCheckConsentDTO =  healthCheckConsentService.getHealthCheckConsent(studentID,date);
            log.info("healthCheckRecordDTO ==> "+healthCheckConsentDTO.toString());

//            Tạo record
            HealthCheckRecordDTO healthCheckRecordDTO = new HealthCheckRecordDTO();
            healthCheckRecordDTO.setHealthCheckConsentId(healthCheckConsentDTO.getId());
//            đẩy qua giao diện
            model.addAttribute("healthCheckRecordDTO",healthCheckRecordDTO);

        }catch (BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "user/listStudentHealthCheck";
        }
        return "admin/healthCheckRecord";
    }

//    Hàm kiểm tra xem đang tạo record hay update và thực hiện gọi hàm tương ứng
@PostMapping("/save-update-Form")
public String controllerAction(@ModelAttribute("healthCheckRecordDTO") @Valid HealthCheckRecordDTO healthCheckRecordDTO,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes,
                                    Model model)
{
    log.info("healthCheckRecordDTO của /save-update-Form: {}", healthCheckRecordDTO);
    if(healthCheckRecordDTO.getId() == 0){
        log.info("chạy cái create");
        return saveHealthCheckRecord(healthCheckRecordDTO,
                bindingResult,
                session,
                redirectAttributes,
                model);
    }else{
        log.info("chạy cái update");
        return updateHealthCheckRecord(healthCheckRecordDTO,
                 bindingResult,
                session,
                redirectAttributes,
                model);
    }

}

    public String saveHealthCheckRecord(@ModelAttribute("healthCheckRecordDTO") @Valid HealthCheckRecordDTO healthCheckRecordDTO,
                                        BindingResult bindingResult,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes,
                                        Model model)
    {
//
//        Lấy HealthCheckConsentDTO phù hợp với id được gửi tới để
        HealthCheckConsentDTO healthCheckConsentDTO;
        try {
            healthCheckConsentDTO = healthCheckConsentService.getHealthCheckConsentById(healthCheckRecordDTO.getHealthCheckConsentId());
        }catch (BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "admin/healthCheckRecord";
        }
        if(bindingResult.hasErrors()){
            return "admin/healthCheckRecord";
        }
        log.info("healthCheckConsentDTO của record: "+ healthCheckConsentDTO);

//        Gán obj healthCheckConsentDTO vừa lấy vô healthCheckRecordDTO
        healthCheckRecordDTO.setHealthCheckConsentDTO(healthCheckConsentDTO);
        log.info("healthCheckRecordDTO của record: "+ healthCheckRecordDTO);

//        Lấy date để có gì còn quay lại đg dẫn trc đó nếu có lỗi
        LocalDate date = healthCheckConsentDTO.getCheckDate();
        log.info("date bên trong record controller: "+date);

//        Lấy id của school nurse
        Long nurseId = SessionUtil.getLoggedInUserId (session);
        log.info("nurseId ==> "+nurseId);

//        Tạo record
        try{
            healthCheckRecordService.create_HealthCheckRecord(healthCheckRecordDTO, nurseId);
            redirectAttributes.addFlashAttribute("success","Kết quả khám đã được ghi nhận thành công!");
            return "redirect:/admin/healthCheckConsent/list-student-health-check?date=" + date;
        }catch (BusinessException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/admin/healthCheckConsent/list-student-health-check?date=" + date;
        }

    }

//    Câpj nhật record
    @GetMapping("/updateForm")
    public String Update_healthCheckRecord(@RequestParam("stdId") Long studentID,
                                    @RequestParam("date") LocalDate date,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        try{
            HealthCheckConsentDTO healthCheckConsentDTO =  healthCheckConsentService.getHealthCheckConsent(studentID,date);
            log.info("healthCheckConsentDTO của updateForm ==> "+healthCheckConsentDTO.toString());

//          Tìm xem record đã có chưa thì mới cho update
            HealthCheckRecordDTO healthCheckRecordDTO;
            try{
                healthCheckRecordDTO = healthCheckRecordService.toHealthCheckRecordDTO(healthCheckConsentDTO);
                healthCheckRecordDTO.setHealthCheckConsentId(healthCheckRecordDTO.getHealthCheckConsentDTO().getId());
                log.info("healthCheckRecordDTO của updateForm ==> "+healthCheckRecordDTO.toString());
            }catch (BusinessException e){
               redirectAttributes.addFlashAttribute("notification",e.getMessage());
                return "redirect:/admin/healthCheckConsent/list-student-health-check?date=" + date;
            }
//            Nếu có thì hiện form với các thông tin có sẵn
//            đẩy qua giao diện
            model.addAttribute("healthCheckRecordDTO",healthCheckRecordDTO);
            return "admin/healthCheckRecord";
        }catch (BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "redirect:/admin/healthCheckConsent/list-student-health-check?date=" + date;
        }

    }

//    Chỉnh sửa thông tin record:
public String updateHealthCheckRecord(@ModelAttribute("healthCheckRecordDTO") @Valid HealthCheckRecordDTO healthCheckRecordDTO_request,
                                    BindingResult bindingResult,
                                      HttpSession session,
                                    RedirectAttributes redirectAttributes,
                                    Model model)
{
    log.info("data của healthCheckRecordDTO khi gửi lên để update: {}", healthCheckRecordDTO_request.toString());
//        Lấy HealthCheckConsentDTO phù hợp với id được gửi tới để
    HealthCheckConsentDTO healthCheckConsentDTO;
    try {
        healthCheckConsentDTO = healthCheckConsentService.getHealthCheckConsentById(healthCheckRecordDTO_request.getHealthCheckConsentId());
    }catch (BusinessException e){
        model.addAttribute("error",e.getMessage());
        return "admin/healthCheckRecord";
    }
    //        Gán obj healthCheckConsentDTO vừa lấy vô healthCheckRecordDTO
    healthCheckRecordDTO_request.setHealthCheckConsentDTO(healthCheckConsentDTO);

    if(bindingResult.hasErrors()){
        return "admin/healthCheckRecord";
    }
    LocalDate date = healthCheckConsentDTO.getCheckDate();
    log.info("date trong health check record update controller = {}",date);
    //        Lấy id của school nurse
        Long nurseId = SessionUtil.getLoggedInUserId (session);
        log.info("nurseId ==> "+nurseId);
    try{
        healthCheckRecordService.update_HealthCheckRecord(healthCheckRecordDTO_request, nurseId);
        redirectAttributes.addFlashAttribute("success","Cập nhật kết quả khám thành công!");
        return "redirect:/admin/healthCheckConsent/list-student-health-check?date=" + date;

    }catch (BusinessException e){
        redirectAttributes.addFlashAttribute("error",e.getMessage());
        return "redirect:/admin/healthCheckConsent/list-student-health-check?date=" + date;
    }

}

}
