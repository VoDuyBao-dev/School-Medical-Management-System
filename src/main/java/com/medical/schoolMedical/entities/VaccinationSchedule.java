package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@Table(name = "vaccination_schedule")
public class VaccinationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private long id;

    @Column(name = "vaccine_type", nullable = false)
    private String vaccine_type;

    @Column(name = "recommended_age_months")
    private String recommended_age_months;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate schedule_date;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;


}

