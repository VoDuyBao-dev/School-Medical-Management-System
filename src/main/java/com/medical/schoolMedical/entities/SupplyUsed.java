package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "supply_used")
public class SupplyUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supply_used_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "medical_supply_id", nullable = false)
    private MedicalSupply medicalSupply;

    @ManyToOne
    @JoinColumn(name = "medical_event_id", nullable = false)
    private MedicalEvent medicalEvent;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}

