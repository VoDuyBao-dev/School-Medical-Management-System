package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sent_medicine")
public class SentMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sent_medicine_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private Parent parent;

    @Column(name = "medicine_list", nullable = false)
    private String medicineList;

    @Column(name = "usage_instructions",columnDefinition = "TEXT", nullable = false)
    private String usageInstructions;

    @Column(name = "sent_date", nullable = false)
    @CreationTimestamp
    private LocalDate sentDate;

}
