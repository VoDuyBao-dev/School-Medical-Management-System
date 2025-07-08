package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@Table(name = "vaccination_schedule")
public class VaccinationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_nurse_id", referencedColumnName = "school_nurse_id", nullable = false)
    @ToString.Exclude
    private SchoolNurse nurse;

    @Column(name = "vaccine_type", nullable = false)
    private String vaccineType;

    @Column(name = "recommended_age_months")
    private String recommendedAgeMonths;

    @Column(name = "injection_date", nullable = false)
    private LocalDateTime injectionDate;

    @Column(name = "sent_date")
    private LocalDate sentDate;

    @Column(name = "create_date", nullable = false)
    @CreationTimestamp
    private LocalDate createDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;


}

