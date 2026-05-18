package com.example.cargotracking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressCreateRequest {

    @NotBlank(message = "Adres başlığı boş bırakılamaz")
    private String addressTitle;

    @NotBlank(message = "Şehir boş bırakılamaz")
    private String city;

    @NotBlank(message = "İlçe boş bırakılamaz")
    private String district;

    @NotBlank(message = "Tam adres alanı boş bırakılamaz")
    private String fullAddress;
}