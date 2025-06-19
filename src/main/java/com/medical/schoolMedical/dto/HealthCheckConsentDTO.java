package com.medical.schoolMedical.dto;

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

@Data
@NoArgsConstructor
public class HealthCheckConsentDTO {
    private long id;
    @ToString.Exclude
    private StudentDTO student;
    @ToString.Exclude
    private Parent parent;
    private String content;
    @FutureOrPresent(message = "Ngày kiểm tra không được là ngày trong quá khứ")
    private LocalDate checkDate;
    private ConsentStatus status;
    private LocalDate sentDate;
    private String notes;
}
