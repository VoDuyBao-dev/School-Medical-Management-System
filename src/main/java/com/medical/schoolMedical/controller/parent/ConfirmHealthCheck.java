package com.medical.schoolMedical.controller.parent;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
@RequestMapping("parent/confirm/confirm-health-check")
public class ConfirmHealthCheck {
    @Autowired
    HealthCheckConsentService healthCheckConsentService;
    @GetMapping
    public String confirmHealthCheck(@RequestParam("idConsent") Long consentId, Model model) {

        try{
            HealthCheckConsentDTO healthCheck = healthCheckConsentService.getHealthCheckConsentById(consentId);
            log.info("healthCheck in confirmHealthCheck: {}", healthCheck);
            model.addAttribute("healthCheckConsent", healthCheck);
            return "parent/confirmHealthCheckConsent";
        }catch(BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "parent/confirmHealthCheckConsent";
        }
    }

    @PostMapping
    public String confirmHealthCheck(@RequestParam("consentId") Long id
            ,@RequestParam("response") String response
            ,Model model) {
        try{
            healthCheckConsentService.updateHealthCheckConsent(id, response);
            return "redirect:/parent/notification/HealthCheckConsents";
        }catch(BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "parent/confirmHealthCheckConsent";
        }
    }
}
