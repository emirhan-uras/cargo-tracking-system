package com.example.cargotracking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchCreateRequest {
    @NotBlank(message = "Şube adı boş bırakılamaz")
    private String name;

    @NotBlank(message = "Şube adresi boş bırakılamaz")
    private String address;

    @NotBlank(message = "Şehir boş bırakılamaz")
    private String city;

    private String phone;
}