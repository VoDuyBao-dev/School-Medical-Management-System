package com.medical.schoolMedical.dto;


import lombok.Data;

@Data
public class SupplyUsedDTO {
    private Long supplyId;
    private String supplyName;
    private int quantity;
    private String notes;
}
