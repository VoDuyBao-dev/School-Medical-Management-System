package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultation_appointment")
public class ConsultationAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_appointment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_nurse_id", referencedColumnName = "school_nurse_id", nullable = false)
    private SchoolNurse schoolNurse;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private AppointmentStatus status;

    @Column(name = "noteFromSchool",columnDefinition = "TEXT")
    private String noteFromSchool;

    @Column(name = "noteFromParent",columnDefinition = "TEXT")
    private String noteFromParent;



}

