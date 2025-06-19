package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.enums.ConsentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckConsentRepository extends JpaRepository<HealthCheckConsent,Long> {
    boolean existsByParent_Id(Long parentId);
    boolean existsByParent_IdAndStatus(Long parentId, ConsentStatus status);
    List<HealthCheckConsent> findByStatus(ConsentStatus status);
    @Query(value = """
    SELECT DISTINCT s FROM HealthCheckConsent h 
    JOIN h.student s 
    WHERE h.checkDate = :date 
    AND h.status = :status 
    AND h.is_checked_health = :is_checked_health 
    ORDER BY s.fullName ASC, s.className ASC 
    """,
            countQuery = """
    SELECT COUNT(DISTINCT s) FROM HealthCheckConsent h 
    JOIN h.student s 
    WHERE h.checkDate = :date 
    AND h.status = :status
    AND h.is_checked_health = :is_checked_health 
    """
    )
    Page<Student> findByCheckDateAndStatusSorted(
            @Param("date") LocalDate date,
            @Param("status") ConsentStatus status,
            @Param("is_checked_health") boolean is_checked_health,
            Pageable pageable
    );

    @Query("""
    SELECT c FROM HealthCheckConsent c 
    WHERE c.sentDate IS NOT NULL AND c.id IN (
        SELECT MIN(c2.id) FROM HealthCheckConsent c2 
        WHERE c2.sentDate IS NOT NULL 
        GROUP BY c2.checkDate
    )
    ORDER BY c.checkDate DESC
""")
    List<HealthCheckConsent> findOneConsentPerCheckDate();

    Optional<HealthCheckConsent> findByStudentIdAndCheckDateAndStatus(Long studentId, LocalDate checkDate, ConsentStatus status);

}
