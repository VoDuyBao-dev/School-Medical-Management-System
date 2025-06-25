package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
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
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            throw new BusinessException(ErrorCode.SAVE_VACCINATION_CONSENT_FAILED);
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

    //    phiếu tiêm chủng chi tiết của từng student
    public VaccinationConsentDTO getVaccinationConsentDTOById(Long vaccinationConsentID) {
        VaccinationConsent consent =  vaccinationConsentRepository.findById(vaccinationConsentID)
                .orElseThrow(() -> new BusinessException(ErrorCode.VACCINATION_CONSENT_NOT_EXISTS));

        return  vaccinationConsentMapper.toVaccinationConsentDTO(consent);
    }

    //    Xử lí trạng thái phiếu tiêm khi người dùng đồng ý hoặc không
    public void updateVaccinationConsent(Long id, String response) {
        VaccinationConsent consent = vaccinationConsentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.VACCINATION_CONSENT_NOT_EXISTS));

        if(consent.getSchedule().getInjectionDate().isBefore(LocalDateTime.now()) || "OVERDUE".equals(consent.getStatus().name())){
            throw new BusinessException(ErrorCode.SURVEY_EXPIRED);
        }
        if("agree".equals(response)){
            consent.setStatus(ConsentStatus.ACCEPTED);
        }else if("disagree".equals(response)){
            consent.setStatus(ConsentStatus.DECLINED);
        }
        try{
            vaccinationConsentRepository.save(consent);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_VACCINATION_CONSENT_FAILED);
        }

    }

//    Hàm tự động cập nhật trạng thái phiêú hết hạn nếu quá ngày

//    public void update_SurveyExpired() {
//        List<HealthCheckConsent> unconfirms = healthCheckConsentRepository.findByStatusWithSchedule(ConsentStatus.UNCONFIRMED);
//        List<HealthCheckConsent> toUpdate = new ArrayList<>();
//        for(HealthCheckConsent healthCheckConsent : unconfirms){
//            if(healthCheckConsent.getSchedule().getCheckDate().isBefore(LocalDateTime.now())){
//                healthCheckConsent.setStatus(ConsentStatus.OVERDUE);
//                toUpdate.add(healthCheckConsent);
//            }
//        }
//        healthCheckConsentRepository.saveAll(toUpdate);
//
//    }
//
//    // Cron job chạy lúc 7h mỗi ngày
//    @Scheduled(cron = "0 0 7 * * *")
//    public void scheduleCheck() {
//        update_SurveyExpired();
//    }
//
//    // Chạy ngay sau khi ứng dụng khởi động
//    @EventListener(ApplicationReadyEvent.class)
//    public void runOnStartup() {
//        update_SurveyExpired();
//    }
//
//    //    Kiểm tra người dùng có phiêú khám sức khỏe nào không
//    public boolean hasNewNotification(Long id) {
//        return healthCheckConsentRepository.existsByParent_IdAndStatus(id, ConsentStatus.UNCONFIRMED);
//
//    }

    //    lấy danh sách các VaccinationConsent của phụ huynh tương ứng
    public Page<VaccinationConsentDTO> getVaccinationConsentByParentId(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 1); // Lấy trang đầu tiên với 20 bản ghi

        Page<VaccinationConsent> vaccinationConsents = vaccinationConsentRepository.findByParent_User_IdOrderByIdDesc(userId,pageable);

//        CHuyển qua Page<VaccinationConsentDTO>
        Page<VaccinationConsentDTO> consentDTOPage = vaccinationConsents.map(vaccinationConsentMapper::toVaccinationConsentDTO);
        log.info("consentDTOPage in getVaccinationConsentByParentId in consentService: {}", consentDTOPage.getContent());
        return consentDTOPage;
    }

}
