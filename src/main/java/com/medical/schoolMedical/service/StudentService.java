package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.StudentMapper;
import com.medical.schoolMedical.enums.Gender;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.repositories.ParentRepositoty;
import com.medical.schoolMedical.repositories.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class StudentService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;

    ParentRepositoty parentRepositoty;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public List<StudentDTO> getAllStudentsDTO() {
        return studentMapper.toStudentDTOs(studentRepository.findAll());
    }

    public List<Student> getByParentId(Long parentId) {
        return studentRepository.findByParent_Id(parentId);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STUDENT_NOT_FOUND));
    }
    public void createStudent(String fullName, Gender gender, LocalDate birthDate,
                              String address, String className, Long parentId) {

        Parent parent = parentRepositoty.findById(parentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_NOT_EXISTS));


        Student student = new Student();
        student.setFullName(fullName);
        student.setGender(gender);
        student.setBirthDate(birthDate);
        student.setAddress(address);
        student.setClassName(className);
        student.setParent(parent);

        studentRepository.save(student);
    }
}
