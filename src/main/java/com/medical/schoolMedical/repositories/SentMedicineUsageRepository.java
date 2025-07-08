package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.SentMedicineUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentMedicineUsageRepository extends JpaRepository<SentMedicineUsage, Long> {
    List<SentMedicineUsage> findBySentMedicine_Id(Long sentMedicineId);
}
