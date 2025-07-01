package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.entities.HealthCheckRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {
HealthCheckConsentMapper.class,
})
public interface HealthCheckRecordMapper {
    @Mappings({
            @Mapping(source = "healthCheckConsent", target = "healthCheckConsentDTO"),
            @Mapping(source = "schoolNurse", target = "schoolNurseDTO"),
            @Mapping(source = "viewedByParent", target = "viewedByParent")

    })
    HealthCheckRecordDTO toHealthCheckRecordDTO(HealthCheckRecord healthCheckRecord);
    @Mappings({
            @Mapping(source = "healthCheckConsentDTO", target = "healthCheckConsent"),
            @Mapping(source = "schoolNurseDTO", target = "schoolNurse")

    })
    HealthCheckRecord toHealthCheckRecord(HealthCheckRecordDTO healthCheckRecordDTO);
//    update
    void updateHealthCheckRecord(@MappingTarget HealthCheckRecord healthCheckRecord, HealthCheckRecord healthCheckRecord_request);
}
