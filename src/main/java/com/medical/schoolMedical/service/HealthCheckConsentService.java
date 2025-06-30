package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.HealthCheckRecordDTO;
import com.medical.schoolMedical.dto.HealthCheckScheduleDTO;
import com.medical.schoolMedical.dto.StudentDTO;
import com.medical.schoolMedical.entities.HealthCheckConsent;
import com.medical.schoolMedical.entities.HealthCheckSchedule;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.enums.ConsentStatus;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.HealthCheckConsentMapper;
import com.medical.schoolMedical.mapper.HealthCheckRecordMapper;
import com.medical.schoolMedical.mapper.HealthCheckScheduleMapper;
import com.medical.schoolMedical.mapper.StudentMapper;
import com.medical.schoolMedical.repositories.HealthCheckConsentRepository;
import com.medical.schoolMedical.repositories.HealthCheckRecordRepository;
import com.medical.schoolMedical.repositories.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HealthCheckConsentService {
    final StudentService studentService;
    final HealthCheckConsentMapper healthCheckConsentMapper;
    final StudentMapper studentMapper;
    final HealthCheckScheduleMapper healthCheckScheduleMapper;
    final HealthCheckConsentRepository healthCheckConsentRepository;

    @Lazy
    final HealthCheckScheduleService healthCheckScheduleService;

    @Lazy
    @Autowired
    HealthCheckRecordService healthCheckRecordService;




    public HealthCheckConsentService(
            StudentService studentService,
            HealthCheckConsentMapper healthCheckConsentMapper,
            StudentMapper studentMapper,
            HealthCheckScheduleMapper healthCheckScheduleMapper,
            HealthCheckConsentRepository healthCheckConsentRepository,
            HealthCheckScheduleService healthCheckScheduleService
    ) {
        this.studentService = studentService;
        this.healthCheckConsentMapper = healthCheckConsentMapper;
        this.studentMapper = studentMapper;
        this.healthCheckScheduleMapper = healthCheckScheduleMapper;
        this.healthCheckConsentRepository = healthCheckConsentRepository;
        this.healthCheckScheduleService = healthCheckScheduleService;
    }

//    Gửi lịch đến phụ huynh:
    public void sendCheckSchedule_toParent(HealthCheckScheduleDTO healthCheckScheduleDTO){
        HealthCheckSchedule healthCheckSchedule = healthCheckScheduleMapper.toHealthCheckSchedule(healthCheckScheduleDTO);
        List<Student> students = studentService.getAllStudents();
        for(Student student : students){
            HealthCheckConsent consent = createConsent(healthCheckSchedule,student);
            try {
                healthCheckConsentRepository.save(consent);
            }catch (Exception e){
                throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_CONSENT_FAILED);
            }
        }
    }

//    dungf để tạo bản ghi consent cho từng học sinh
    private HealthCheckConsent createConsent(HealthCheckSchedule healthCheckSchedule, Student student) {
        return HealthCheckConsent.builder()
                .schedule(healthCheckSchedule)
                .student(student)
                .parent(student.getParent())
                .status(ConsentStatus.UNCONFIRMED)
                .build();
    }

    //    Phân trang:
    private Pageable pagination(int page, int size) {
        //        số lượng học sinh trên 1 trang danh sách
        int pageSize = size;
//        Định nghĩa Pageable
//        cần tham số là: trang a cần lấy và số lg hs trên 1 trang
        return PageRequest.of(page, pageSize);
    }

//    lấy danh sách các consent của phụ huynh tương ứng
    public Page<HealthCheckConsentDTO> getHealthCheckConsentByParentId(Long userId, int page) {
        Pageable pageable = pagination(page, 20); // Lấy trang đầu tiên với 20 bản ghi

        Page<HealthCheckConsent> healthCheckConsents = healthCheckConsentRepository.findByParent_User_IdOrderByIdDesc(userId,pageable);

//        CHuyển qua Page<HealthCheckConsentDTO>
        Page<HealthCheckConsentDTO> consentDTOPage = healthCheckConsents.map(healthCheckConsentMapper::toDTO);
        log.info("consentDTOPage in getHealthCheckConsentByParentId in consentService: {}", consentDTOPage.getContent());
        return consentDTOPage;
    }

//    Lấy thông tin của phiếu khám sức khỏe hiển thị bên phía phụ huynh
//    phiếu chi tiết của từng student
    public HealthCheckConsentDTO getHealthCheckConsentById(Long healthCheckID) {
        HealthCheckConsent healthCheck =  healthCheckConsentRepository.findById(healthCheckID)
                .orElseThrow(() -> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND));

        return  healthCheckConsentMapper.toDTO(healthCheck);
    }

//    Xử lí trạng thái phiếu khám khi người dùng đồng ý hoặc không
    public void updateHealthCheckConsent(Long id, String response) {
        HealthCheckConsent healthCheckConsent = healthCheckConsentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND));

        if(healthCheckConsent.getSchedule().getCheckDate().isBefore(LocalDateTime.now()) || "OVERDUE".equals(healthCheckConsent.getStatus().name())){
            throw new BusinessException(ErrorCode.SURVEY_EXPIRED);
        }
        if("agree".equals(response)){
            healthCheckConsent.setStatus(ConsentStatus.ACCEPTED);
        }else if("disagree".equals(response)){
            healthCheckConsent.setStatus(ConsentStatus.DECLINED);
        }
        try{
            healthCheckConsentRepository.save(healthCheckConsent);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SAVE_HEALTH_CHECK_CONSENT_FAILED);
        }

    }

//    Hàm tự động cập nhật trạng thái phiêú hết hạn nếu quá ngày

    public void update_SurveyExpired() {
        List<HealthCheckConsent> unconfirms = healthCheckConsentRepository.findByStatusWithSchedule(ConsentStatus.UNCONFIRMED);
        List<HealthCheckConsent> toUpdate = new ArrayList<>();
        for(HealthCheckConsent healthCheckConsent : unconfirms){
            if(healthCheckConsent.getSchedule().getCheckDate().isBefore(LocalDateTime.now())){
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

//    Lấy toàn bộ học sinh đã được accepted theo ngày kiểm tra sức khỏe đã khám và chưa khám
    public Page<HealthCheckConsentDTO> getStudentsHealthCheck(Long scheduleId, int page, boolean is_checked_health) {

        Pageable pageable = pagination(page, 20);

//        Lấy lịch phù hợp với scheduleId
        HealthCheckSchedule schedule =  null;
        try{
            HealthCheckScheduleDTO scheduleDTO = healthCheckScheduleService.getHealthCheckScheduleById(scheduleId);
            schedule = healthCheckScheduleMapper.toHealthCheckSchedule(scheduleDTO);
        }catch (BusinessException e){
            throw new BusinessException(e.getErrorCode());
        }
        log.info("schedule in getStudentsHealthCheck: {}", schedule);


//        Lấy danh sách student theo consent, dùng consent để truy suất student tương ứng
        Page<HealthCheckConsent> listConsent = healthCheckConsentRepository
                .listConsentsByScheduleAndStatusAndCheckState(schedule, ConsentStatus.ACCEPTED,is_checked_health, pageable);

        log.info("listConsent in getStudentsHealthCheck: {}", listConsent.getContent());

//        CHuyển qua Page<HealthCheckConsentDTO>
        Page<HealthCheckConsentDTO> consentDTOPage = listConsent.map(healthCheckConsentMapper::toDTO);
        return consentDTOPage;
    }
//
//    Lấy health check consent phù hợp với cái người dùng chọn
    public HealthCheckConsentDTO getHealthCheckConsentDTO_ById(Long healthCheckID) {
        HealthCheckConsent consent = healthCheckConsentRepository.findById(healthCheckID)
                .orElseThrow(() -> new  BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND));

        return healthCheckConsentMapper.toDTO(consent);

    }
//
     public HealthCheckConsent getHealthCheckConsentEntity_ById(Long healthCheckID) {
        HealthCheckConsent healthCheck =  healthCheckConsentRepository.findById(healthCheckID)
                .orElseThrow(() -> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND));

        return  healthCheck;
    }

//    Lấy health check consent theo id
//    public HealthCheckConsentDTO getHealthCheckConsentByID(Long id) {
//        HealthCheckConsent result = healthCheckConsentRepository.findById(id)
//                .orElseThrow(()-> new BusinessException(ErrorCode.HEALTH_CHECK_CONSENT_NOT_FOUND,
//                        "Không tìm thấy bản ghi phù hợp của khảo sát khám sức khỏe có id " + id));
//        return healthCheckConsentMapper.toHealthCheckConsentDTO(result);
//    }
}
