package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.dto.VaccinationScheduleDTO;
import com.medical.schoolMedical.entities.HealthCheckSchedule;
import com.medical.schoolMedical.entities.VaccinationSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccinationScheduleMapper {
    VaccinationSchedule toVaccinationSchedule(VaccinationScheduleDTO vaccinationScheduleDTO);
    VaccinationScheduleDTO toVaccinationScheduleDTO(VaccinationSchedule VaccinationSchedule);
}
