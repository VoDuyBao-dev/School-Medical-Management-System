package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.SchoolNurse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolNurseRepository extends JpaRepository<SchoolNurse,Integer> {
}
