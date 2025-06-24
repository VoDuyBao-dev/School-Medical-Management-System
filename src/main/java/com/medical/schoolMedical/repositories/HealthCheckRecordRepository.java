package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.HealthCheckRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckRecordRepository extends JpaRepository<HealthCheckRecord,Long> {
    Optional<HealthCheckRecord> findByHealthCheckConsent(HealthCheckConsent healthCheckConsent);
    Optional<HealthCheckRecord> findById(long id);

    @Query("SELECT r FROM HealthCheckRecord r WHERE r.healthCheckConsent.id IN :consentIds")
    List<HealthCheckRecord> findByConsentIds(@Param("consentIds") List<Long> consentIds);


    boolean existsByHealthCheckConsent_Parent_IdAndSentToParentTrueAndViewedByParentFalse(Long parentId);

}
