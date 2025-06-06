package com.medical.schoolMedical.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Data
@NoArgsConstructor
@Table(name = "parents")
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "fullname", length = 50)
    private String fullName;

    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;


}

