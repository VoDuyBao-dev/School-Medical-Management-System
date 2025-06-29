package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.MedicineUsed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineUsedRepository  extends JpaRepository<MedicineUsed, Long> {
    List<MedicineUsed> findByMedicalEventId(Long eventId);
}
