package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "vaccination_record")
public class VaccinationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccination_record_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_consent_id", referencedColumnName = "vaccination_consent_id", nullable = false)
    @ToString.Exclude
    private VaccinationConsent vaccinationConsent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_nurse_id", referencedColumnName = "school_nurse_id", nullable = false)
    @ToString.Exclude
    private SchoolNurse schoolNurse;


    @Column(name = "post_vaccination_condition",nullable = false)
    private String postVaccinationCondition;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}

