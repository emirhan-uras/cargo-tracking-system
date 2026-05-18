package com.example.cargotracking.dto.response;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String addressTitle;
    private String city;
    private String district;
    private String fullAddress;
    private Long userId;
}