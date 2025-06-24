package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.entities.*;
import com.medical.schoolMedical.enums.ConsentStatus;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.HealthCheckRecordMapper;
import com.medical.schoolMedical.mapper.HealthCheckScheduleMapper;
import com.medical.schoolMedical.repositories.HealthCheckScheduleRepository;
import com.medical.schoolMedical.repositories.SchoolNurseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HealthCheckScheduleService {
    final HealthCheckScheduleRepository healthCheckScheduleRepository;
    final HealthCheckScheduleMapper healthCheckScheduleMapper;
    final StudentService studentService;
    final SchoolNurseRepository schoolNurseRepository;

    @Lazy
    @Autowired
    HealthCheckConsentService healthCheckConsentService;

    public HealthCheckScheduleDTO create_checkSchedule(HealthCheckScheduleDTO healthCheckScheduleDTO, Long userId) {
        if(healthCheckScheduleDTO.getCheckDate().isBefore(LocalDateTime.now())){
            throw new BusinessException(ErrorCode.CHECK_DATE_INVALID);
        }
        log.info("healthCheckScheduleDTO in create: {}", healthCheckScheduleDTO);
        HealthCheckSchedule healthCheckSchedule = healthCheckScheduleMapper.toHealthCheckSchedule(healthCheckScheduleDTO);
        log.info("healthCheckSchedule in create: {}", healthCheckSchedule);

//        Lấy Nurse phù hợp với userId
        SchoolNurse nurse = schoolNurseRepository.findByUser_Id(userId).orElseThrow(()->
                new BusinessException(ErrorCode.SCHOOL_NURSE_NOT_EXISTS));

        healthCheckSchedule.setNurse(nurse);

        try{
            return healthCheckScheduleMapper.toHealthCheckScheduleDTO(healthCheckScheduleRepository.save(healthCheckSchedule));
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_SCHEDULE_FAILED);
        }

    }

    //    Lấy các lịch khám sức khỏe đã gửi
    public Page<HealthCheckScheduleDTO> getAllHealthCheckSchedule(int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));

        Page<HealthCheckSchedule> healthCheckSchedule = healthCheckScheduleRepository.findAll(pageable);
        Page<HealthCheckScheduleDTO> healthCheckScheduleDTOPage = healthCheckSchedule.map(healthCheckScheduleMapper::toHealthCheckScheduleDTO);
        log.info("healthCheckScheduleDTOPage in getAllHealthCheckSchedule: {}", healthCheckScheduleDTOPage.getContent());
        return healthCheckScheduleDTOPage;
    }

//    Lấy Schedule theo id
    public HealthCheckScheduleDTO getHealthCheckScheduleById(Long id) {
        HealthCheckSchedule healthCheckSchedule = healthCheckScheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.HEALTH_CHECK_SCHEDULE_NOT_EXISTS));
        return healthCheckScheduleMapper.toHealthCheckScheduleDTO(healthCheckSchedule);
    }


}
