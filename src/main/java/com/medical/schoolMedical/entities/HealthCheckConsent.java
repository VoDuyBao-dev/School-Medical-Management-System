package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "health_check_consent")
public class HealthCheckConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_consent_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @Column(name = "check_date",nullable = false)
    private LocalDate checkDate;

    @Column(name = "confirmed",columnDefinition = "TINYINT DEFAULT 0",nullable = false)
    private boolean confirmed = false;

    @Column(name = "sent_date", nullable = false)
    @CreationTimestamp
    private LocalDate sentDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}

