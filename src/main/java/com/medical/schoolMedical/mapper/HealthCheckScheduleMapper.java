package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.HealthCheckSchedule;
import com.medical.schoolMedical.entities.SchoolNurse;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface HealthCheckScheduleMapper {

    HealthCheckScheduleDTO toHealthCheckScheduleDTO(HealthCheckSchedule healthCheckSchedule);
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "className", target = "className"),
            @Mapping(source = "checkDate", target = "checkDate"),
            @Mapping(source = "sentDate", target = "sentDate"),
            @Mapping(source = "notes", target = "notes"),
            @Mapping(source = "healthCheckConsent", target = "healthCheckConsent"),
            @Mapping(source = "nurse", target = "nurse")

    })
    HealthCheckSchedule toHealthCheckSchedule(HealthCheckScheduleDTO healthCheckScheduleDTO);

    List<HealthCheckScheduleDTO> toListHealthCheckScheduleDTO(List<HealthCheckSchedule> healthCheckSchedules);

}
