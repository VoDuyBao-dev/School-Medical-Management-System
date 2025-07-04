package com.medical.schoolMedical.service;


import com.medical.schoolMedical.entities.Medicine;
import com.medical.schoolMedical.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thuốc với ID: " + id));
    }

    public void saveMedicine(Medicine medicine) {
        medicineRepository.save(medicine);
    }
/*
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }*/



    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    public void updateQuantity(Long id, int delta) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        if (medicine != null) {
            int newQty = medicine.getQuantityInStock() + delta;
            medicine.setQuantityInStock(Math.max(newQty, 0)); // Không cho âm
            medicineRepository.save(medicine);
        }
    }

    public boolean existsByName(String name) {
        return medicineRepository.findByNameIgnoreCase(name).isPresent();
    }

    public boolean isNameTakenByOtherId(String name, Long id) {
        return medicineRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), id);
    }

}
