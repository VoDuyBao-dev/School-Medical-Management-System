package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    Optional<Manager> findByUser_Username(String username);

}
