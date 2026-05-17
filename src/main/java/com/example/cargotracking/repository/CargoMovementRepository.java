package com.example.cargotracking.repository;

import com.example.cargotracking.entity.CargoMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CargoMovementRepository extends JpaRepository<CargoMovement, Long> {

    List<CargoMovement> findByCargoIdOrderByMovementDateAsc(Long cargoId);
}