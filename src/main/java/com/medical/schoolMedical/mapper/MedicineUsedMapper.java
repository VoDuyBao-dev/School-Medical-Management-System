package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.MedicineUsedDTO;
import com.medical.schoolMedical.entities.MedicineUsed;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineUsedMapper {
    public static MedicineUsedDTO toDTO(MedicineUsed entity) {
        MedicineUsedDTO dto = new MedicineUsedDTO();
        dto.setMedicineId(entity.getMedicine().getId());
        dto.setMedicineName(entity.getMedicine().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    // Nếu bạn cần chuyển ngược lại từ DTO → Entity (ít dùng hơn)
    public static MedicineUsed toEntity(MedicineUsedDTO dto) {
        MedicineUsed entity = new MedicineUsed();
        // Không set ID hoặc liên kết nếu dùng để tạo mới
        entity.setQuantity(dto.getQuantity());
        entity.setNotes(dto.getNotes());
        return entity;
    }
}
