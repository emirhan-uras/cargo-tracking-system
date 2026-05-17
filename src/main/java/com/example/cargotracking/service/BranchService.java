package com.example.cargotracking.service;

import com.example.cargotracking.dto.request.BranchCreateRequest;
import com.example.cargotracking.dto.request.BranchCreateRequest;
import com.example.cargotracking.dto.response.BranchResponse;
import com.example.cargotracking.entity.Branch;
import com.example.cargotracking.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchResponse createBranch(BranchCreateRequest request) {
        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setCity(request.getCity());
        branch.setPhone(request.getPhone());

        Branch savedBranch = branchRepository.save(branch);
        return convertToResponse(savedBranch);
    }

    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public BranchResponse getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Şube bulunamadı! ID: " + id));
        return convertToResponse(branch);
    }

    public void deleteBranch(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new RuntimeException("Silinmek istenen şube bulunamadı! ID: " + id);
        }
        branchRepository.deleteById(id);
    }

    private BranchResponse convertToResponse(Branch branch) {
        BranchResponse response = new BranchResponse();
        response.setId(branch.getId());
        response.setName(branch.getName());
        response.setAddress(branch.getAddress());
        response.setCity(branch.getCity());
        response.setPhone(branch.getPhone());
        return response;
    }
}