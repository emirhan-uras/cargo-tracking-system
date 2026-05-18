package com.example.cargotracking.dto.response;

import com.example.cargotracking.entity.enums.CargoStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CargoResponse {
    private Long id;
    private String trackingNumber;
    private String senderUsername;
    private String receiverName;
    private String receiverPhone;
    private String deliveryAddress;
    private CargoStatus currentStatus;
    private LocalDateTime pickupTime;
    private Double estimatedPrice;
    private String currentBranchName;
    private String assignedCourierName;
}