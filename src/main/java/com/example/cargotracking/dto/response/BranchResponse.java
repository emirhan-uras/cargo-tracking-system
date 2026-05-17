package com.example.cargotracking.dto.response;

import lombok.Data;

@Data
public class BranchResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String phone;
}