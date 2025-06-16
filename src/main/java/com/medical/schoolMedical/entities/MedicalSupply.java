package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "medical_supply")
public class MedicalSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "medical_supply_id")
    private long id;

    // Quan hệ với bảng trung gian
    @OneToMany(mappedBy = "medicalSupply", cascade = CascadeType.ALL)
    private List<SupplyUsed> supplyUsed;

    @Column(name = "name", length = 50,nullable = false)
    private String name;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;

}
