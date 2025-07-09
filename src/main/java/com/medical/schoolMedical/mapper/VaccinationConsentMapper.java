package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.VaccinationConsentDTO;
import com.medical.schoolMedical.entities.VaccinationConsent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccinationConsentMapper {
    @Mapping(source = "vaccinationRecord.sentToParent", target = "sentToParent")
    VaccinationConsentDTO toVaccinationConsentDTO(VaccinationConsent vaccinationConsent);
    VaccinationConsent toVaccinationConsent(VaccinationConsentDTO vaccinationConsentDTO);
}
