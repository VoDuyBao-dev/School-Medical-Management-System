package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.entities.VaccinationSchedule;
import com.medical.schoolMedical.enums.ConsentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class VaccinationConsentDTO {
    private long id;
    @ToString.Exclude
    private Student student;
    @ToString.Exclude
    private Parent parent;
    @ToString.Exclude
    private VaccinationSchedule schedule;
    private ConsentStatus status;
    private boolean vaccinated = false;
    private Boolean sentToParent;

}
