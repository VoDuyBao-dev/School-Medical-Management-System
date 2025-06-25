package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.VaccinationConsentDTO;
import com.medical.schoolMedical.dto.VaccinationScheduleDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.entities.VaccinationConsent;
import com.medical.schoolMedical.entities.VaccinationSchedule;
import com.medical.schoolMedical.enums.ConsentStatus;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.VaccinationConsentMapper;
import com.medical.schoolMedical.mapper.VaccinationScheduleMapper;
import com.medical.schoolMedical.repositories.StudentRepository;
import com.medical.schoolMedical.repositories.VaccinationConsentRepository;
import com.medical.schoolMedical.repositories.VaccinationScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VaccinationConsentService {
    VaccinationConsentRepository vaccinationConsentRepository;
    VaccinationScheduleMapper vaccinationScheduleMapper;
    VaccinationScheduleRepository vaccinationScheduleRepository;
    VaccinationConsentMapper vaccinationConsentMapper;
    StudentService studentService;

    //    Gửi lịch đến phụ huynh:
    public void sendVaccinationSchedule_toParent(VaccinationScheduleDTO vaccinationScheduleDTO){
        VaccinationSchedule vaccinationSchedule = vaccinationScheduleMapper.toVaccinationSchedule(vaccinationScheduleDTO);
        List<Student> students = studentService.getAllStudents();
        List<VaccinationConsent> consentList = new ArrayList<>();
        for(Student student : students){
            VaccinationConsent consent = createVaccinationConsent(vaccinationSchedule,student);
           consentList.add(consent);
        }
        try {
            vaccinationConsentRepository.saveAll(consentList);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_CONSENT_FAILED);
        }
    }

    //    dungf để tạo bản ghi consent cho từng học sinh
    private VaccinationConsent createVaccinationConsent(VaccinationSchedule vaccinationSchedule, Student student) {
        return VaccinationConsent.builder()
                .schedule(vaccinationSchedule)
                .student(student)
                .parent(student.getParent())
                .status(ConsentStatus.UNCONFIRMED)
                .build();
    }

}
