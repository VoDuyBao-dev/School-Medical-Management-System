package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.enums.ConsentStatus;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.HealthCheckConsentMapper;
import com.medical.schoolMedical.mapper.StudentMapper;
import com.medical.schoolMedical.repositories.HealthCheckConsentRepository;
import com.medical.schoolMedical.repositories.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class HealthCheckConsentService {
    StudentRepository studentRepository;
    HealthCheckConsentMapper healthCheckConsentMapper;
    StudentMapper studentMapper;
    HealthCheckConsentRepository healthCheckConsentRepository;

    public boolean create_checkConsent(HealthCheckConsentDTO healthCheckConsentDTO) {
            List<Student> students = studentRepository.findAll();
            for(Student student : students){
                HealthCheckConsent consent = createConsentFromDTO(healthCheckConsentDTO,student);
                 try {
                     healthCheckConsentRepository.save(consent);
                 }catch (Exception e){
                     throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_CONSENT_FAILED, "Tạo phiếu kiểm tra sức khỏe thất bại");
                 }
            }
            return true;
    }

    private HealthCheckConsent createConsentFromDTO(HealthCheckConsentDTO healthCheckConsentDTO, Student student) {
        return HealthCheckConsent.builder()
                .student(student)
                .parent(student.getParent())
                .content(healthCheckConsentDTO.getContent())
                .status(ConsentStatus.UNCONFIRMED)
                .checkDate(healthCheckConsentDTO.getCheckDate())
                .notes(healthCheckConsentDTO.getNotes())
                .build();
    }

//    Lấy thông tin của phiếu khám sức khỏe hiển thị bên phía phụ huynh
//    phiếu chi tiết của từng student
    public HealthCheckConsentDTO getHealthCheckConsentById(Long healthCheckID) {
        HealthCheckConsent healthCheck =  healthCheckConsentRepository.findById(healthCheckID)
                .orElseThrow(() -> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND, "ID của phiếu đồng ý kiểm tra sức khỏe không hợp lệ"));

        return  healthCheckConsentMapper.toDTO(healthCheck);
    }

//    Xử lí trạng thái phiếu khám khi người dùng đồng ý hoặc không
    public void updateHealthCheckConsent(Long id, String response) {
        HealthCheckConsent healthCheckConsent = healthCheckConsentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND, "ID của phiếu kiểm tra sức khỏe không hợp lệ"));

        if(healthCheckConsent.getCheckDate().isBefore(LocalDate.now()) || "OVERDUE".equals(healthCheckConsent.getStatus().name())){
            throw new BusinessException(ErrorCode.SURVEY_EXPIRED, "Phiếu kiểm tra sức khỏe đã hết hạn");
        }
        if("agree".equals(response)){
            healthCheckConsent.setStatus(ConsentStatus.ACCEPTED);
        }else if("disagree".equals(response)){
            healthCheckConsent.setStatus(ConsentStatus.DECLINED);
        }
        try{
            healthCheckConsentRepository.save(healthCheckConsent);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_CONSENT_FAILED, "update phiếu kiểm tra sức khỏe thất bại");
        }

    }
//    Hàm tự động cập nhật trạng thái phiêú hết hạn nếu quá ngày
    public void update_SurveyExpired() {
        List<HealthCheckConsent> unconfirms = healthCheckConsentRepository.findByStatus(ConsentStatus.UNCONFIRMED);
        List<HealthCheckConsent> toUpdate = new ArrayList<>();
        for(HealthCheckConsent healthCheckConsent : unconfirms){
            if(healthCheckConsent.getCheckDate().isBefore(LocalDate.now())){
                healthCheckConsent.setStatus(ConsentStatus.OVERDUE);
                toUpdate.add(healthCheckConsent);
            }
        }
        healthCheckConsentRepository.saveAll(toUpdate);

    }

    // Cron job chạy lúc 7h mỗi ngày
    @Scheduled(cron = "0 0 7 * * *")
    public void scheduleCheck() {
        update_SurveyExpired();
    }

    // Chạy ngay sau khi ứng dụng khởi động
    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        update_SurveyExpired();
    }

//    Kiểm tra người dùng có phiêú khám sức khỏe nào không
    public boolean hasNewNotification(Long id) {
        return healthCheckConsentRepository.existsByParent_IdAndStatus(id, ConsentStatus.UNCONFIRMED);

    }


//    Lấy toàn bộ học sinh đã được accepted theo ngày kiểm tra sức khỏe
    public Page<StudentDTO> getStudentsHealthCheck(LocalDate checkDate, int page) {
//        số lượng học sinh trên 1 trang danh sách
        int pageSize = 20;
//        Định nghĩa Pageable
//        cần tham số là: trang a cần lấy và số lg hs trên 1 trang
        Pageable pageable = PageRequest.of(page, pageSize);
//        Lấy danh sách các student health check trong ngày ... đã được phụ huynh đồng ý
        Page<Student> studentPage = healthCheckConsentRepository
                .findByCheckDateAndStatusSorted(checkDate, ConsentStatus.ACCEPTED, pageable);

//        Chuyển các student đó sang studentDTO
        List<StudentDTO> dtoList = studentPage.getContent().stream()
                .map(studentMapper::toStudentDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, studentPage.getTotalElements());

    }

//    Lấy các lịch khám sức khỏe đã gửi
    public List<HealthCheckConsentDTO> getAllHealthCheckConsents() {

        return healthCheckConsentMapper.toHealthCheckConsentDTOs(healthCheckConsentRepository.findOneConsentPerCheckDate());
    }

//    Lấy health check consent phù hợp với cái người dùng chọn
    public HealthCheckConsentDTO getHealthCheckConsent(Long studentId, LocalDate checkDate) {
        HealthCheckConsent consent = healthCheckConsentRepository.findByStudentIdAndCheckDateAndStatus(studentId,checkDate,ConsentStatus.ACCEPTED)
                .orElseThrow(() -> new  BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND, "không tìm thấy lịch khám sức khỏe theo yêu cầu"));

        return healthCheckConsentMapper.toDTO(consent);

    }

//    Lấy health check consent theo id
//    public HealthCheckConsentDTO getHealthCheckConsentByID(Long id) {
//        HealthCheckConsent result = healthCheckConsentRepository.findById(id)
//                .orElseThrow(()-> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND,
//                        "Không tìm thấy bản ghi phù hợp của khảo sát khám sức khỏe có id " + id));
//        return healthCheckConsentMapper.toHealthCheckConsentDTO(result);
//    }
}
