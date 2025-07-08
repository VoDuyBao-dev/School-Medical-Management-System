package com.medical.schoolMedical.repositories;


import com.medical.schoolMedical.entities.SentMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentMedicineRepository extends JpaRepository<SentMedicine, Long> {
    List<SentMedicine> findByParent_User_Id(Long userId);
}
