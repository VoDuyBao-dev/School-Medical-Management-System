package com.medical.schoolMedical.entities;

import com.medical.schoolMedical.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "isDeleted", columnDefinition = "TINYINT DEFAULT 0")
    private boolean isDeleted = false;

    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDate creationDate;

    // Optional: ánh xạ ngược 1-1 (chỉ nếu cần)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Thêm orphanRemoval cho 1-1 (xoa cha se xoa con)
    @ToString.Exclude
    private Parent parent;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Manager manager;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private SchoolNurse nurse;

}

