package com.example.cargotracking.entity;

import com.example.cargotracking.entity.enums.CargoStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cargos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false, length = 1000)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoStatus currentStatus;

    @ManyToOne
    @JoinColumn(name = "current_branch_id")
    private Branch currentBranch;

    @ManyToOne
    @JoinColumn(name = "assigned_courier_id")
    private User assignedCourier;


    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
    private List<CargoMovement> movements;
}