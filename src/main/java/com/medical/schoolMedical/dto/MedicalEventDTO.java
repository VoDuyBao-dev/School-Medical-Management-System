package com.medical.schoolMedical.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalEventDTO {
    private Long id; // Dùng cho list + xem chi tiết

    private Long studentId;
    private String studentFullName; // Dùng cho list
    private String nurseFullName;

    private Long nurseId;
    private String location;
    private String description;
    private String initialTreatment;
    private String finalTreatment;
    private String notes;

    private Timestamp eventTime; // Dùng cho list

}
