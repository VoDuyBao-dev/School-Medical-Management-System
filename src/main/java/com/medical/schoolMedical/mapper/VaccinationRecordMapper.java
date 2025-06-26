package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.VaccinationRecordDTO;
import com.medical.schoolMedical.entities.VaccinationRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccinationRecordMapper {
    VaccinationRecordDTO toVaccinationRecordDTO(VaccinationRecord vaccinationRecord);
    VaccinationRecord toVaccinationRecord(VaccinationRecordDTO vaccinationRecordDTO);
}
