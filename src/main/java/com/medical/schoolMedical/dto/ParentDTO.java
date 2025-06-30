package com.medical.schoolMedical.dto;

import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParentDTO {
    private long id;
    @ToString.Exclude
    private UserDTO userDTO;
    private List<Student> students = new ArrayList<>();
    private String fullName;
    private String phoneNumber;
    private String address;

}
