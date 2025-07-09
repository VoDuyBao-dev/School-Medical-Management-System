package com.medical.schoolMedical.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Component
public class ConsentStatusSchedulerService {
    private final VaccinationConsentService vaccinationConsentService;
    private final HealthCheckConsentService healthCheckConsentService;

    @Autowired
    public ConsentStatusSchedulerService(VaccinationConsentService vaccinationConsentService, HealthCheckConsentService healthCheckConsentService) {
        this.vaccinationConsentService = vaccinationConsentService;
        this.healthCheckConsentService = healthCheckConsentService;
    }

    // Cron job chạy lúc 7h mỗi ngày
    @Scheduled(cron = "0 0 7 * * *")
    public void scheduleCheck() {
        vaccinationConsentService.update_SurveyExpired_VaccinationConsent();
        healthCheckConsentService.update_SurveyExpired_HealthCheckConsent();
    }

    // Chạy ngay sau khi ứng dụng khởi động
    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        vaccinationConsentService.update_SurveyExpired_VaccinationConsent();
        healthCheckConsentService.update_SurveyExpired_HealthCheckConsent();
    }
}

