package com.medical.schoolMedical.service;


import com.medical.schoolMedical.dto.MedicalEventDTO;
import com.medical.schoolMedical.entities.MedicalEvent;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.repositories.MedicalEventRepository;
import com.medical.schoolMedical.repositories.StudentRepository;
import com.medical.schoolMedical.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalEventService {

    private final MedicalEventRepository medicalEventRepository;

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    //Lưu và cập nhật sự kiện y tế
    public void saveMedicalEvent(MedicalEvent event) {
        medicalEventRepository.save(event);
    }

    //Lấy tất cả sự kiện y tế
    public List<MedicalEvent> getAllMedicalEvents() {
        return medicalEventRepository.findAll();
    }

    //Tìm sự kiện y tế theo id
    public MedicalEvent findMedicalEventById(Long id) {
        return medicalEventRepository.findById(id).orElse(null);
    }

    //Tìm sự kiên của một học sinh
    public List<MedicalEvent> findByStudentId(Long studentId) {
        return medicalEventRepository.findByStudentId(studentId);
    }

    //Tìm các sự kiện do một y tá phụ trách
    public List<MedicalEvent> findByNurseId(Long nurseId) {
        return medicalEventRepository.findBySchoolNurseId(nurseId);
    }

    //Xóa sự kiện
    public void deleteMedicalEventById(Long id) {
        medicalEventRepository.deleteById(id);
    }

    // Chuyển từ Entity sang DTO
    public MedicalEventDTO convertToDto(MedicalEvent event) {
        MedicalEventDTO dto = new MedicalEventDTO();
        dto.setId(event.getId());
        dto.setStudentId(event.getStudent().getId());
        dto.setStudentFullName(event.getStudent().getFullName());
        dto.setEventTime(event.getEventTime());
        dto.setLocation(event.getLocation());
        dto.setDescription(event.getDescription());
        dto.setInitialTreatment(event.getInitial_treatment());
        dto.setFinalTreatment(event.getFinal_treatment());
        dto.setNotes(event.getNotes());
        return dto;
    }

    // Cập nhật entity từ DTO
    public void updateFromDto(MedicalEventDTO dto) {
        Optional<MedicalEvent> optionalEvent = medicalEventRepository.findById(dto.getId());
        if (optionalEvent.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy sự kiện y tế có ID: " + dto.getId());
        }

        MedicalEvent event = optionalEvent.get();

        // Lấy lại thông tin học sinh
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học sinh"));

        event.setStudent(student);
        event.setLocation(dto.getLocation());
        event.setDescription(dto.getDescription());
        event.setInitial_treatment(dto.getInitialTreatment());
        event.setFinal_treatment(dto.getFinalTreatment());
        event.setNotes(dto.getNotes());

        medicalEventRepository.save(event);
    }




}
