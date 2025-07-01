package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepositoty extends JpaRepository<Parent,Long> {
    Optional<Parent> findByUser_Id(Long userId);

    Optional<Parent> findByUser_Username(String username);

}
