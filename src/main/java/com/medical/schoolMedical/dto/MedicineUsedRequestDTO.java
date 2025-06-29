package com.medical.schoolMedical.dto;

import lombok.Data;

@Data
public class MedicineUsedRequestDTO {
    private Long medicineId;
    private int quantity;
    private String notes;
}
