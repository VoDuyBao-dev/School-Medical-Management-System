package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.ParentDTO;
import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParentMapper {
    ParentDTO toParentDTO(Parent parent);
}
