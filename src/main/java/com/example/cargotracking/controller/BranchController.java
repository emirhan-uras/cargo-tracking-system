package com.example.cargotracking.controller;

import com.example.cargotracking.dto.request.BranchCreateRequest;
import com.example.cargotracking.dto.request.BranchCreateRequest;
import com.example.cargotracking.dto.response.BranchResponse;
import com.example.cargotracking.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchResponse> createBranch(@Valid @RequestBody BranchCreateRequest request) {
        BranchResponse response = branchService.createBranch(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        List<BranchResponse> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable Long id) {
        BranchResponse response = branchService.getBranchById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok("Şube başarıyla silindi.");
    }
}