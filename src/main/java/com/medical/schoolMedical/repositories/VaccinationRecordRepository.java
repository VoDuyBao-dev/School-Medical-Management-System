package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord,Long> {
}
