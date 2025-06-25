package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.VaccinationScheduleDTO;
import com.medical.schoolMedical.entities.HealthCheckSchedule;
import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.entities.VaccinationSchedule;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.VaccinationScheduleMapper;
import com.medical.schoolMedical.repositories.SchoolNurseRepository;
import com.medical.schoolMedical.repositories.VaccinationScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class VaccinationScheduleService {
    final VaccinationScheduleRepository vaccinationScheduleRepository;
    final VaccinationScheduleMapper vaccinationScheduleMapper;
    final SchoolNurseRepository schoolNurseRepository;

    public VaccinationScheduleDTO createVaccinationSchedule(VaccinationScheduleDTO vaccinationScheduleDTO, Long userId) {
        VaccinationSchedule vaccinationSchedule = vaccinationScheduleMapper.toVaccinationSchedule(vaccinationScheduleDTO);
        log.info("vaccinationSchedule in createVaccinationSchedule: {}", vaccinationSchedule);

//        Lấy Nurse phù hợp với userId
        SchoolNurse nurse = schoolNurseRepository.findByUser_Id(userId).orElseThrow(()->
                new BusinessException(ErrorCode.SCHOOL_NURSE_NOT_EXISTS));

        vaccinationSchedule.setNurse(nurse);

        try{
            VaccinationScheduleDTO result = vaccinationScheduleMapper.toHealthCheckScheduleDTO(vaccinationScheduleRepository.save(vaccinationSchedule));
            return result;
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_VACCINATION_SCHEDULE_FAILED);
        }
    }
}
