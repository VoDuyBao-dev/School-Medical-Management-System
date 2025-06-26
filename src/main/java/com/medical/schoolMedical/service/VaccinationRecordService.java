package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.dto.VaccinationRecordDTO;
import com.medical.schoolMedical.entities.*;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.VaccinationRecordMapper;
import com.medical.schoolMedical.repositories.SchoolNurseRepository;
import com.medical.schoolMedical.repositories.VaccinationRecordRepository;
import com.medical.schoolMedical.repositories.VaccinationScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VaccinationRecordService {
    VaccinationRecordRepository vaccinationRecordRepository;
    VaccinationScheduleService service;
    VaccinationRecordMapper vaccinationRecordMapper;
    SchoolNurseRepository schoolNurseRepository;
    VaccinationConsentService vaccinationConsentService;

    private VaccinationRecord vaccinationRecord_fullInfor(VaccinationRecordDTO vaccinationRecordDTO, Long userID){
        //Chuyển kiểu để lưu form:
        VaccinationRecord vaccinationRecord =  vaccinationRecordMapper.toVaccinationRecord(vaccinationRecordDTO);

//        tìm school nurse phù hơp và gán vào trường schoolNurse trong đối tượng vaccinationRecord
        SchoolNurse schoolNurse = schoolNurseRepository.findByUser_Id(userID).orElseThrow(()->
                new BusinessException(ErrorCode.SCHOOL_NURSE_NOT_EXISTS));
//        Gán lại
        vaccinationRecord.setSchoolNurse(schoolNurse);
        return vaccinationRecord;
    }

    public void create_VaccinationRecord(VaccinationRecordDTO vaccinationRecordDTO,Long vaccinationConsentID, Long userID) {
        VaccinationConsent vaccinationConsent = null;
        VaccinationRecord vaccinationRecord = null;
        try{
            vaccinationConsent = vaccinationConsentService.getVaccinationConsentEntity_ById(vaccinationConsentID);
        }catch (BusinessException e){
            throw new BusinessException(e.getErrorCode());
        }

        try{
            vaccinationRecord = vaccinationRecord_fullInfor(vaccinationRecordDTO, userID);
            vaccinationRecord.setVaccinationConsent(vaccinationConsent);
//            Cập nhật bản vaccination consent tương ứng để biết học sinh đó đã có bản ghi kết quả
            vaccinationRecord.getVaccinationConsent().setVaccinated(true);
            log.info("Dữ liệu của đối tượng vaccinationRecord trong create: {}", vaccinationRecord);

            try{
                VaccinationRecord result = vaccinationRecordRepository.save(vaccinationRecord);
                log.info("Save VaccinationRecord==> "+result);
            }catch (Exception e){
                throw new BusinessException(ErrorCode.SAVE_VACCINATION_RECORD_FAILED);
            }
        }catch (BusinessException e){
            throw new BusinessException(e.getErrorCode());
        }


    }
}
