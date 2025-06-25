package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.VaccinationConsentDTO;
import com.medical.schoolMedical.entities.VaccinationConsent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccinationConsentMapper {
    VaccinationConsentDTO toVaccinationConsentDTO(VaccinationConsent vaccinationConsent);
    VaccinationConsent toVaccinationConsent(VaccinationConsentDTO vaccinationConsentDTO);
}
