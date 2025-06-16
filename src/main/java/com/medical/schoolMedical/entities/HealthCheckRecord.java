package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Health_check_record")
public class HealthCheckRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "health_check_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_check_consent_id", referencedColumnName = "health_check_consent_id", nullable = false)
    private HealthCheckConsent healthCheckConsent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_nurse_id", referencedColumnName = "school_nurse_id", nullable = false)
    private SchoolNurse schoolNurse;

    @Column(name = "vision_result", nullable = false)
    private int visionResult ;

    @Column(name = "hearing_result", nullable = false)
    private String hearingResult;

    @Column(name = "other_result")
    private String otherResult;

    @Column(name = "assessment")
    private String assessment;

    @Column(name = "needs_consultation",columnDefinition = "TINYINT DEFAULT 0",nullable = false)
    private boolean needs_consultation = false;
}

