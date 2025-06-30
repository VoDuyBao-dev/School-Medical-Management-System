package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.HealthCheckRecord;
import com.medical.schoolMedical.entities.HealthCheckSchedule;
import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.enums.ConsentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class HealthCheckConsentDTO {
    private long id;
    @ToString.Exclude
    private StudentDTO student;
    @ToString.Exclude
    private Parent parent;
    @ToString.Exclude
    private HealthCheckSchedule schedule;
    private Long healthCheckRecordId;
    private ConsentStatus status;
    private boolean checkedHealth = false;




}
