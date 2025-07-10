package com.medical.schoolMedical.repositories;

import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findAll();

    List<Student> findByParent(Parent parent);

    List<Student> findByParent_Id(Long parentId);

    List<Student> findByClassNameStartingWith(String classNamePrefix);





}
