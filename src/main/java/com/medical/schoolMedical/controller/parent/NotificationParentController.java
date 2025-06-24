package com.medical.schoolMedical.controller.parent;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.HealthCheckConsentService;
import com.medical.schoolMedical.service.HealthCheckRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/parent/notification")
public class NotificationParentController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;

    @Autowired
    private HealthCheckRecordService healthCheckRecordService;

    @GetMapping("/HealthCheckConsents")
    public String listHealthCheckConsent(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestParam(defaultValue = "0") int page,
                                         Model model) {
        Long userId = customUserDetails.getUser().getId();
        log.info("parentId: {}", userId);

        Page<HealthCheckConsentDTO> listConsent = healthCheckConsentService.getHealthCheckConsentByParentId(userId, page);

        log.info("listHealthCheckConsent in consent Parent: {}", listConsent.getContent());

        model.addAttribute("listHealthCheckConsent", listConsent.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listConsent.getTotalPages());
        return "parent/listHealthCheckConsent";

    }

    @GetMapping("/HealthCheckRecords")
    public String listHealthCheckRecord(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestParam(defaultValue = "0") int page,
                                         Model model) {
        Long userId = customUserDetails.getUser().getId();
        log.info("parentId: {}", userId);

        Page<HealthCheckRecordDTO> listRecord = healthCheckRecordService.getSentRecordsToParents(userId, page);

        model.addAttribute("listHealthCheckRecord", listRecord.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listRecord.getTotalPages());
        return "parent/listHealthCheckRecord";

    }
}
