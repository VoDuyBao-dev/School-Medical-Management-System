package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.VaccinationConsent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationConsentRepository extends JpaRepository<VaccinationConsent, Long> {
}
