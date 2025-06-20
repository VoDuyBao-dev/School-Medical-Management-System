package com.medical.schoolMedical.entities;

import com.medical.schoolMedical.enums.ConsentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude = {"student", "parent"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "health_check_consent")
public class HealthCheckConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "health_check_consent_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    @ToString.Exclude
    private Parent parent;

    @ToString.Exclude
    @OneToOne(mappedBy = "healthCheckConsent", fetch = FetchType.LAZY)
    private HealthCheckRecord healthCheckRecord;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "check_date",nullable = false)
    private LocalDate checkDate;

    @Column(name = "consent_status", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'UNCONFIRMED'")
    @Enumerated(EnumType.STRING)
    private ConsentStatus status;

    @Column(name = "sent_date", nullable = false)
    @CreationTimestamp
    private LocalDate sentDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_checked_health",columnDefinition = "TINYINT DEFAULT 0",nullable = false)
    private boolean checkedHealth = false;
}

