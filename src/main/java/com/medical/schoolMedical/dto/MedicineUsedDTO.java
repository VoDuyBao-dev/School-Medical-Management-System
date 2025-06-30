package com.medical.schoolMedical.dto;


import lombok.Data;

@Data
public class MedicineUsedDTO {
    private Long medicineId;
    private String medicineName;
    private int quantity;
    private String notes;

}
