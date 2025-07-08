package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class StudentDTO {
    private long id;
    private long healthCheck_recordId;
    @ToString.Exclude
    private Parent parent;
    private String fullName;
    private Gender gender;
    private LocalDate birthDate;
    private String address;
    private String className;

}
