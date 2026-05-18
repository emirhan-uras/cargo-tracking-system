package com.example.cargotracking.dto.response;

import com.example.cargotracking.entity.enums.CargoStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CargoMovementResponse {
    private Long id;
    private CargoStatus status;
    private LocalDateTime timestamp;
    private String branchName;
    private String description;
}