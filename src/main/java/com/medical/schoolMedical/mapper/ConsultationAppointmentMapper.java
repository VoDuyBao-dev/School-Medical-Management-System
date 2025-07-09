package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.ConsultationAppointmentDTO;
import com.medical.schoolMedical.entities.ConsultationAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ConsultationAppointmentMapper {
    ConsultationAppointment toConsultationAppointment(ConsultationAppointmentDTO consultationAppointmentDTO);

    @Mapping(target = "studentDTO", source = "student")
    @Mapping(target = "schoolNurseDTO", source = "schoolNurse")
    ConsultationAppointmentDTO toConsultationAppointmentDTO(ConsultationAppointment consultationAppointment);
}
