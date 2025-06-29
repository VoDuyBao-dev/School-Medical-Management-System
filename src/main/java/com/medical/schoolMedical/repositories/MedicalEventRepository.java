package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.MedicalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicalEventRepository extends JpaRepository<MedicalEvent, Long> {
    List<MedicalEvent> findAll();
    List<MedicalEvent> findByStudentId(Long studentId);
    List<MedicalEvent> findBySchoolNurseId(Long nurseId);
    @Query("SELECT e FROM MedicalEvent e " +
            "LEFT JOIN FETCH e.medicineUsed " +
            "LEFT JOIN FETCH e.supplyUsed " +
            "WHERE e.id = :id")
    Optional<MedicalEvent> findByIdWithDetails(@Param("id") Long id);

}
