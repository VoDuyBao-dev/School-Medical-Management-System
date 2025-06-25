package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.VaccinationConsent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationConsentRepository extends JpaRepository<VaccinationConsent, Long> {
    //    Lấy các vaccinationConsent của parent tươnhg ưnhgs
    Page<VaccinationConsent> findByParent_User_IdOrderByIdDesc(Long userId, Pageable pageable);
}
