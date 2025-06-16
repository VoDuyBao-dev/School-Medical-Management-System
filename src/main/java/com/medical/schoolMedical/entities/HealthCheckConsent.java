package com.medical.schoolMedical.entities;

import com.medical.schoolMedical.enums.ConsentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@ToString(exclude = {"student", "parent"}) // nếu cần
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
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

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
}

