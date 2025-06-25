package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.VaccinationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule,Long> {
}
