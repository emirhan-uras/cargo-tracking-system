package com.example.cargotracking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}