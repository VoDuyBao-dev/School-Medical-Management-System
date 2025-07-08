package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.Admin;
import com.medical.schoolMedical.entities.SchoolNurse;

import com.medical.schoolMedical.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolNurseRepository extends JpaRepository<SchoolNurse,Long> {
    Optional<SchoolNurse> findByUser_Id(long id);

    Optional<SchoolNurse> findByUser_Username(String username);

    long user(User user);
}
