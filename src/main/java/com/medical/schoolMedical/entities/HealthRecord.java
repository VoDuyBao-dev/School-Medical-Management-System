package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "health_record")
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "health_record_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @Column(name = "allergies ", nullable = false)
    private String allergies ;

    @Column(name = "chronic_disease ", nullable = false)
    private String chronicDisease ;

    @Column(name = "treatment_history ", columnDefinition = "TEXT", nullable = false)
    private String treatmentHistory ;

    @Column(name = "vision", nullable = false)
    private int vision ;

    @Column(name = "hearing", nullable = false)
    private String hearing;

    @Column(name = "vaccination", nullable = false)
    private String vaccination;

    @Column(name = "other_health_info", columnDefinition = "TEXT")
    private String other_health_info;

}

