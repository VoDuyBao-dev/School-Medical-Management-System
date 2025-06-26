package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.HealthCheckRecord;
import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.HealthCheckConsentMapper;
import com.medical.schoolMedical.mapper.HealthCheckRecordMapper;
import com.medical.schoolMedical.repositories.HealthCheckConsentRepository;
import com.medical.schoolMedical.repositories.HealthCheckRecordRepository;
import com.medical.schoolMedical.repositories.SchoolNurseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.peek;

@Service

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HealthCheckRecordService {

    @Lazy
    @Autowired
    HealthCheckConsentService healthCheckConsentService;
    final HealthCheckRecordRepository healthCheckRecordRepository;
    final SchoolNurseRepository schoolNurseRepository;
    final HealthCheckRecordMapper healthCheckRecordMapper;
    final HealthCheckConsentMapper healthCheckConsentMapper;

    // Constructor không chứa healthCheckConsentService để tránh vòng lặp
    public HealthCheckRecordService(HealthCheckRecordRepository healthCheckRecordRepository,
                                    SchoolNurseRepository schoolNurseRepository,
                                    HealthCheckRecordMapper healthCheckRecordMapper,
                                    HealthCheckConsentMapper healthCheckConsentMapper) {
        this.healthCheckRecordRepository = healthCheckRecordRepository;
        this.schoolNurseRepository = schoolNurseRepository;
        this.healthCheckRecordMapper = healthCheckRecordMapper;
        this.healthCheckConsentMapper = healthCheckConsentMapper;
    }

    private HealthCheckRecord healthCheckRecord_fullInfor(HealthCheckRecordDTO healthCheckRecordDTO, Long userId){
        //Chuyển kiểu để lưu form:
        HealthCheckRecord healthCheckRecord =  healthCheckRecordMapper.toHealthCheckRecord(healthCheckRecordDTO);

//        tìm school nurse phù hơp và gán vào trường schoolNurse trong đối tượng healthCheckRecord
        SchoolNurse schoolNurse = schoolNurseRepository.findByUser_Id(userId).orElseThrow(()->
                new BusinessException(ErrorCode.SCHOOL_NURSE_NOT_EXISTS));
//        Gán lại
        healthCheckRecord.setSchoolNurse(schoolNurse);
        return healthCheckRecord;
    }

//    Tìm obj HealthCheckRecord theo id
    public HealthCheckRecordDTO getHealthCheckRecordById(long id){
        HealthCheckRecord healthCheckRecord = healthCheckRecordRepository.findById(id)
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_EXISTS));
        return healthCheckRecordMapper.toHealthCheckRecordDTO(healthCheckRecord);
    }


    public void create_HealthCheckRecord(HealthCheckRecordDTO healthCheckRecordDTO,Long consentID ,Long userId) {
        HealthCheckConsent healthCheckConsent = null;
        HealthCheckRecord healthCheckRecord = null;
        try{
            healthCheckConsent = healthCheckConsentService.getHealthCheckConsentEntity_ById(consentID);
        }catch (BusinessException e){
            throw new BusinessException(e.getErrorCode());
        }

        try{
            healthCheckRecord = healthCheckRecord_fullInfor(healthCheckRecordDTO, userId);
            healthCheckRecord.setHealthCheckConsent(healthCheckConsent);
//            Cập nhật bản health check consent tương ứng để biết học sinh đó đã có bản ghi kết quả
            healthCheckRecord.getHealthCheckConsent().setCheckedHealth(true);
            log.info("Dữ liệu của đối tượng healthCheckRecord trong create: {}", healthCheckRecord);
            try{
                HealthCheckRecord result = healthCheckRecordRepository.save(healthCheckRecord);
                log.info("Save HealthCheckRecord==> "+result);
            }catch (Exception e){
                throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_RECORD_FAILED);
            }
        }catch (BusinessException e){
            throw new BusinessException(ErrorCode.SCHOOL_NURSE_NOT_EXISTS);
        }


    }
//Lấy bản ghi record tương ứng
    private HealthCheckRecord toHealthCheckRecord(HealthCheckConsent healthCheckConsent) {
        return healthCheckRecordRepository.
                findByHealthCheckConsent(healthCheckConsent)
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_EXISTS));

    }
//    Kiểm tra bản đã có record của bản consent chưa
    public HealthCheckRecordDTO toHealthCheckRecordDTO(HealthCheckConsentDTO healthCheckConsentDTO) {
        HealthCheckConsent healthCheckConsent = healthCheckConsentMapper.toEntity(healthCheckConsentDTO);
        HealthCheckRecord healthCheckRecord;
        try{
            healthCheckRecord = toHealthCheckRecord(healthCheckConsent);
            return healthCheckRecordMapper.toHealthCheckRecordDTO(healthCheckRecord);
        }catch (BusinessException e){
            throw new BusinessException(e.getErrorCode());

        }

    }

    public void update_HealthCheckRecord(HealthCheckRecordDTO healthCheckRecordDTO, Long nurseId) {
        HealthCheckRecord healthCheckRecord = null;
        try {
            healthCheckRecord = healthCheckRecord_fullInfor(healthCheckRecordDTO, nurseId);
            log.info("Dữ liệu của đối tượng healthCheckRecord trong update: {}", healthCheckRecord);
        }catch (BusinessException e){
            throw new BusinessException(ErrorCode.SCHOOL_NURSE_NOT_EXISTS);
        }
        //    Giữ cho is_checked_health của bản ghi đó vẫn là true nếu không nó tự động chuyển false làm sai dữ liệu
        healthCheckRecord.getHealthCheckConsent().setCheckedHealth(true);
//        lấy bản ghi cần update:
        HealthCheckRecord healthCheckRecord_needUpdate = healthCheckRecordRepository.findById(healthCheckRecordDTO.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_EXISTS));
        log.info("Dữ liệu của đối tượng healthCheckRecord_needUpdate: {}", healthCheckRecord_needUpdate);
//        Tiến hành gán dữ liệu để update
        healthCheckRecordMapper.updateHealthCheckRecord(healthCheckRecord_needUpdate, healthCheckRecord);
        try{
            HealthCheckRecord result = healthCheckRecordRepository.save(healthCheckRecord_needUpdate);
            log.info("Save HealthCheckRecord==> "+result);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_RECORD_FAILED);
        }
    }

// Gửi cho phụ huynh:
    public void sendRecordsToParents(List<Long> consentIds){
        List<HealthCheckRecord> recordsToUpdate = healthCheckRecordRepository.findByConsentIds(consentIds);
        if (recordsToUpdate.size() != consentIds.size()) {
            throw new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_EXISTS);
        }
        recordsToUpdate.forEach(record -> record.setSentToParent(true));
        healthCheckRecordRepository.saveAll(recordsToUpdate);


        healthCheckRecordRepository.saveAll(recordsToUpdate);

    }

//    Lấy danh sách các record đã được gửi đến phụ huynh
    public Page<HealthCheckRecordDTO> getSentRecordsToParents(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 20);

        Page<HealthCheckRecord> sentRecords = healthCheckRecordRepository.findBySentToParentTrueAndHealthCheckConsent_Parent_User_Id(userId, pageable);

        Page<HealthCheckRecordDTO> sentRecordsDTO = sentRecords.map(healthCheckRecordMapper::toHealthCheckRecordDTO);
        log.info("list record Sent to parents: {}", sentRecordsDTO.getContent());
        return sentRecordsDTO;
    }

//    Lấy health check record tương ứng và cập nhật nó là parent đã xem:
    public HealthCheckRecordDTO getCheckRecord_updateViewed(long id){
        HealthCheckRecord healthCheckRecord = healthCheckRecordRepository.findById(id)
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_EXISTS));

//        update viewedByParent to true
        healthCheckRecord.setViewedByParent(true);
        try{
            healthCheckRecordRepository.save(healthCheckRecord);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_RECORD_FAILED);
        }

        return healthCheckRecordMapper.toHealthCheckRecordDTO(healthCheckRecord);
    }

}
