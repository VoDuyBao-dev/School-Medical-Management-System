package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.HealthRecord;
import com.medical.schoolMedical.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    Optional<HealthRecord> findByStudent_Id(Long studentId);

    Optional<HealthRecord> findByStudent(Student student);
    boolean existsByStudent(Student student);

    List<HealthRecord> findAll();

    @Query("SELECT h FROM HealthRecord h WHERE LOWER(h.student.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<HealthRecord> searchByStudentName(@Param("keyword") String keyword);

    @Query("SELECT hr FROM HealthRecord hr " +
            "JOIN FETCH hr.student s " +
            "JOIN FETCH hr.parent p " +
            "WHERE hr.id = :id")
    Optional<HealthRecord> findByIdWithStudentAndParent(@Param("id") Long id);

}
