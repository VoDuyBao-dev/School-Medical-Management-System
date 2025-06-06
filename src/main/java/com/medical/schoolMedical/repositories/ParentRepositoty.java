package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepositoty extends JpaRepository<Parent,Integer> {
}
