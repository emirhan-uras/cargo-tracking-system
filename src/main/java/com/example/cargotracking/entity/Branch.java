package com.example.cargotracking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE branches SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 500)
    private String address;

    @Column
    private String phone;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}