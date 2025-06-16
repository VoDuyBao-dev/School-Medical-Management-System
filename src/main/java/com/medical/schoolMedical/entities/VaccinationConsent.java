package com.medical.schoolMedical.entities;

import com.medical.schoolMedical.enums.ConsentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "vaccination_consent")
public class VaccinationConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vaccination_consent_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id", nullable = false)
    private VaccinationSchedule schedule;

    @Column(name = "consent_status", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'UNCONFIRMED'")
    @Enumerated(EnumType.STRING)
    private ConsentStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
