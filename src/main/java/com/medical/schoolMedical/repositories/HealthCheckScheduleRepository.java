package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.entities.HealthCheckSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckScheduleRepository extends JpaRepository<HealthCheckSchedule, Long> {
    Page<HealthCheckSchedule> findAll(Pageable pageable);
}
