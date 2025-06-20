package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.HealthCheckRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthCheckRecordRepository extends JpaRepository<HealthCheckRecord,Long> {
    Optional<HealthCheckRecord> findByHealthCheckConsent(HealthCheckConsent healthCheckConsent);
    Optional<HealthCheckRecord> findById(long id);

}
