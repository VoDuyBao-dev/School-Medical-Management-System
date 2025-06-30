package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.entities.VaccinationConsent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class VaccinationRecordDTO {

    private long id;

    @ToString.Exclude
    private VaccinationConsentDTO vaccinationConsent;
    @ToString.Exclude
    private SchoolNurse schoolNurse;
    private long vaccinationConsentId;
    @NotBlank(message = "Không được để trống tình trạng sau tiêm chủng")
    private String postVaccinationCondition;
    private String notes;
    private boolean sentToParent = false;
    private boolean viewedByParent  = false;
}
