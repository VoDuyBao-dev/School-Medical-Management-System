package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.MedicalEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalEventRepository extends JpaRepository<MedicalEvent, Long> {
    List<MedicalEvent> findAll();
    List<MedicalEvent> findByStudentId(Long studentId);
    List<MedicalEvent> findBySchoolNurseId(Long nurseId);
}
