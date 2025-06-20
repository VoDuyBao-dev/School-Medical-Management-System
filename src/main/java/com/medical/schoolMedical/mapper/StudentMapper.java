package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.Student;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    List<StudentDTO> toStudentDTOs(List<Student> students);
    StudentDTO toStudentDTO(Student student);

}
