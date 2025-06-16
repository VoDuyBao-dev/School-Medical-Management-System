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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class HealthCheckRecordService {
    HealthCheckConsentService healthCheckConsentService;
    HealthCheckRecordRepository healthCheckRecordRepository;
    SchoolNurseRepository schoolNurseRepository;
    HealthCheckRecordMapper healthCheckRecordMapper;
    private final HealthCheckConsentMapper healthCheckConsentMapper;


    private HealthCheckRecord healthCheckRecord_fullInfor(HealthCheckRecordDTO healthCheckRecordDTO, Long nurseId){
        //Chuyển kiểu để lưu form:
        HealthCheckRecord healthCheckRecord =  healthCheckRecordMapper.toHealthCheckRecord(healthCheckRecordDTO);

//        tìm school nurse phù hơp và gán vào trường schoolNurse trong đối tượng healthCheckRecord
        SchoolNurse schoolNurse = schoolNurseRepository.findByUser_Id(nurseId).orElseThrow(()->
                new BusinessException(ErrorCode.NOT_FOUND_SCHOOL_NURSE,"Không tìm thấy school nurse phù hợp"));
//        Gán lại
        healthCheckRecord.setSchoolNurse(schoolNurse);
        return healthCheckRecord;
    }

//    Tìm obj HealthCheckRecord theo id
    public HealthCheckRecordDTO getHealthCheckRecordById(long id){
        HealthCheckRecord healthCheckRecord = healthCheckRecordRepository.findById(id)
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_FOUND,"Lỗi không tìm thấy bản ghi của health check record phù hợp!"));
        return healthCheckRecordMapper.toHealthCheckRecordDTO(healthCheckRecord);
    }


    public void create_HealthCheckRecord(HealthCheckRecordDTO healthCheckRecordDTO, Long nurseId) {
        HealthCheckRecord healthCheckRecord = null;
        try{
            healthCheckRecord = healthCheckRecord_fullInfor(healthCheckRecordDTO, nurseId);
            log.info("Dữ liệu của đối tượng healthCheckRecord trong create: {}", healthCheckRecord);
            try{
                HealthCheckRecord result = healthCheckRecordRepository.save(healthCheckRecord);
                log.info("Save HealthCheckRecord==> "+result);
            }catch (Exception e){
                throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_RECORD_FAILED, "Kết quả ghi nhận không thành công!");
            }
        }catch (BusinessException e){
            throw new BusinessException(ErrorCode.NOT_FOUND_SCHOOL_NURSE,e.getMessage());
        }


    }
//Lấy bản ghi record tương ứng
    private HealthCheckRecord toHealthCheckRecord(HealthCheckConsent healthCheckConsent) {
        return healthCheckRecordRepository.
                findByHealthCheckConsent(healthCheckConsent)
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_FOUND,
                        "Bạn chưa có bản ghi nhận khám sức khỏe nào! Vui lòng thêm bản ghi."));

    }
//    Kiểm tra bản đã có record của bản consent chưa
    public HealthCheckRecordDTO toHealthCheckRecordDTO(HealthCheckConsentDTO healthCheckConsentDTO) {
        HealthCheckConsent healthCheckConsent = healthCheckConsentMapper.toEntity(healthCheckConsentDTO);
        HealthCheckRecord healthCheckRecord;
        try{
            healthCheckRecord = toHealthCheckRecord(healthCheckConsent);
            return healthCheckRecordMapper.toHealthCheckRecordDTO(healthCheckRecord);
        }catch (BusinessException e){
            throw new BusinessException(e.getErrorCode(), e.getMessage());

        }



    }

    public void update_HealthCheckRecord(HealthCheckRecordDTO healthCheckRecordDTO, Long nurseId) {
        HealthCheckRecord healthCheckRecord = null;
        try {
            healthCheckRecord = healthCheckRecord_fullInfor(healthCheckRecordDTO, nurseId);
            log.info("Dữ liệu của đối tượng healthCheckRecord trong update: {}", healthCheckRecord);
        }catch (BusinessException e){
            throw new BusinessException(ErrorCode.NOT_FOUND_SCHOOL_NURSE,e.getMessage());
        }
//        lấy bản ghi cần update:
        HealthCheckRecord healthCheckRecord_needUpdate = healthCheckRecordRepository.findById(healthCheckRecordDTO.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.HEALTH_CHECK_RECORD_NOT_FOUND,"Lỗi không tìm thấy bản ghi của health check record phù hợp!"));
        log.info("Dữ liệu của đối tượng healthCheckRecord_needUpdate: {}", healthCheckRecord_needUpdate);
//        Tiến hành gán dữ liệu để update
        healthCheckRecordMapper.updateHealthCheckRecord(healthCheckRecord_needUpdate, healthCheckRecord);
        try{
            HealthCheckRecord result = healthCheckRecordRepository.save(healthCheckRecord_needUpdate);
            log.info("Save HealthCheckRecord==> "+result);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_RECORD_FAILED, "Chỉnh sửa ghi nhận không thành công!");
        }
    }

}
