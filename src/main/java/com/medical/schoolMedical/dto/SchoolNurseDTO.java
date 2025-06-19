package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class SchoolNurseDTO {
    private long id;
    @ToString.Exclude
    private User user;
    private String fullName;
    private int experience;
}
