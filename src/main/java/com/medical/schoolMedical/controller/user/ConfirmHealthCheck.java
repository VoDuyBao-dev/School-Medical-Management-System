package com.medical.schoolMedical.controller.user;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.mapper.HealthCheckConsentMapper;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/confirm/confirm-health-check")
public class ConfirmHealthCheck {
    @Autowired
    HealthCheckConsentService healthCheckConsentService;
    @GetMapping("/{id}")
    public String confirmHealthCheck(@PathVariable Long id, Model model) {
        try{
            HealthCheckConsentDTO healthCheck = healthCheckConsentService.getHealthCheckConsentById(id);
            model.addAttribute("healthCheckConsent", healthCheck);
            return "user/confirmHealthCheckConsent";
        }catch(BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "user/confirmHealthCheckConsent";
        }
    }

    @PostMapping
    public String confirmHealthCheck(@RequestParam("consentId") Long id
            ,@RequestParam("response") String response
            ,Model model) {
        try{
            healthCheckConsentService.updateHealthCheckConsent(id, response);
            return "redirect:/home";
        }catch(BusinessException e){
            model.addAttribute("error",e.getMessage());
            return "user/confirmHealthCheckConsent";
        }
    }
}
