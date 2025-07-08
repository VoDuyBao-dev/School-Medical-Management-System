package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.StudentMapper;
import com.medical.schoolMedical.repositories.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class StudentService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public List<StudentDTO> getAllStudentsDTO() {
        return studentMapper.toStudentDTOs(studentRepository.findAll());
    }


}
