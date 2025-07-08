package com.medical.schoolMedical.service;

import com.medical.schoolMedical.entities.MedicalSupply;
import com.medical.schoolMedical.repositories.MedicalSupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalSupplyService {

    @Autowired
    private MedicalSupplyRepository medicalSupplyRepository;

    public List<MedicalSupply> getAllSupplies() {
        return medicalSupplyRepository.findAll();
    }

    public MedicalSupply getSupplyById(Long id) {
        return medicalSupplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vật tư với ID: " + id));
    }

    public List<MedicalSupply> getAllMedicalSupplies() {
        return medicalSupplyRepository.findAll();
    }

    public void save(MedicalSupply supply) {
        medicalSupplyRepository.save(supply);
    }

    public void deleteById(Long id) {
        medicalSupplyRepository.deleteById(id);
    }



    public boolean existsByName(String name) {
        return medicalSupplyRepository.findByNameIgnoreCase(name.trim()).isPresent();
    }

    public boolean isNameTakenByOtherId(String name, Long id) {
        return medicalSupplyRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), id);
    }


}
