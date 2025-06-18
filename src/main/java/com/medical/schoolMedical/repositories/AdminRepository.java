package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Optional<Admin> findByUser_Username(String username);

}
