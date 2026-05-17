package com.example.cargotracking.entity;

import com.example.cargotracking.entity.enums.CargoStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cargo_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargoMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoStatus status;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime movementDate;

    @PrePersist
    protected void onCreate() {
        this.movementDate = LocalDateTime.now();
    }
}