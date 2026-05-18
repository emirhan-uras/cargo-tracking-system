package com.example.cargotracking.controller;

import com.example.cargotracking.dto.request.CargoCreateRequest;
import com.example.cargotracking.dto.response.CargoDetailResponse;
import com.example.cargotracking.dto.response.CargoResponse;
import com.example.cargotracking.service.CargoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;

    @PostMapping("/request-courier")
    public ResponseEntity<CargoResponse> createCourierRequest(@Valid @RequestBody CargoCreateRequest request) {
        CargoResponse response = cargoService.createCourierRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CargoResponse>> getAllCargos() {
        return ResponseEntity.ok(cargoService.getAllCargos());
    }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<CargoDetailResponse> getCargoDetails(@PathVariable String trackingNumber) {
        CargoDetailResponse response = cargoService.getCargoDetailsByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(response);
    }
}