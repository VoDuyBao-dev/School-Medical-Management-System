package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HealthCheckConsentMapper {
    HealthCheckConsent toEntity(HealthCheckConsentDTO healthCheckConsentDTO);
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "parent", target = "parent"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "checkDate", target = "checkDate"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "sentDate", target = "sentDate"),
            @Mapping(source = "notes", target = "notes")
    })
    HealthCheckConsentDTO toDTO(HealthCheckConsent healthCheckConsent);
    List<HealthCheckConsentDTO> toHealthCheckConsentDTOs(List<HealthCheckConsent> healthCheckConsents);
}
