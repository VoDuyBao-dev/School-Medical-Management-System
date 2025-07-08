package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.VaccinationSchedule;
import jakarta.persistence.Column;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule,Long> {
    Page<VaccinationSchedule> findBySentToParent(boolean sentToParent, Pageable pageable);

}
