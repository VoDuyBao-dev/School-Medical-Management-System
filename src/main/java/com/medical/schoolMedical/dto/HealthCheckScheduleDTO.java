package com.medical.schoolMedical.dto;


import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.SchoolNurse;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class HealthCheckScheduleDTO {
    private long id;

    @ToString.Exclude
    private SchoolNurse nurse;

    @ToString.Exclude
    private List<HealthCheckConsent> healthCheckConsent;
    private String content;
    private LocalDateTime checkDate;
    private LocalDate sentDate;
    private String notes;

    private boolean sentToParent;

// ngày và giờ để ghép lại thành LocalDateTime checkDate; phù hợp
    private String date;
    private String time;



}
