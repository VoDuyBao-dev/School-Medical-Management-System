package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.enums.ConsentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ConsultationAppointmentDTO {
    private Long id;
    private StudentDTO studentDTO;
//    dùng cái này nhận id cho nhanh
    private Long studentId;
    private SchoolNurseDTO schoolNurseDTO;
    @FutureOrPresent(message = "Ngày tiêm phải là thời điểm ở hiện tại hoặc tương lai")
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
    private LocalDate sentDate;
    private ConsentStatus status;
    private String content;
}
