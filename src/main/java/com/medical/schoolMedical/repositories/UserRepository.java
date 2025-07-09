package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.BitSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
    List<User> findByIsDeletedFalse();
    boolean existsByEmail(String email);
    User findByEmail(String email);

}

