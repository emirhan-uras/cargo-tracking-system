package com.example.cargotracking.service;

import com.example.cargotracking.dto.request.CargoCreateRequest;
import com.example.cargotracking.dto.response.CargoDetailResponse;
import com.example.cargotracking.dto.response.CargoResponse;
import com.example.cargotracking.entity.Branch;
import com.example.cargotracking.entity.Cargo;
import com.example.cargotracking.entity.User;
import com.example.cargotracking.entity.enums.CargoStatus;
import com.example.cargotracking.repository.BranchRepository;
import com.example.cargotracking.repository.CargoRepository;
import com.example.cargotracking.repository.SystemSettingRepository;
import com.example.cargotracking.repository.UserRepository;
import com.example.cargotracking.service.external.OpenRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargoService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;
    private final CargoRepository cargoRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final OpenRouteService openRouteService;

    public CargoResponse createCourierRequest(CargoCreateRequest request) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Gönderici kullanıcı bulunamadı!"));


        Branch branch = branchRepository.findById(request.getCurrentBranchId())
                .orElseThrow(() -> new RuntimeException("Seçilen şube bulunamadı!"));

        Cargo cargo = new Cargo();

        cargo.setTrackingNumber("CRG-" + UUID.randomUUID().toString().substring(0, 7).toUpperCase());

        cargo.setSender(sender);
        cargo.setReceiverName(request.getReceiverName());
        cargo.setReceiverPhone(request.getReceiverPhone());

        String fullDeliveryAddress = request.getCity() + " / " + request.getDistrict() + " / " + request.getStreetAndDetails();
        cargo.setDeliveryAddress(fullDeliveryAddress);

        cargo.setCurrentStatus(CargoStatus.REQUESTED);
        cargo.setPickupTime(request.getPickupTime());
        cargo.setCurrentBranch(branch);

        Double calculatedPrice = calculateDistanceAndPrice(branch.getCity() + " " + branch.getName(), fullDeliveryAddress);
        cargo.setEstimatedPrice(calculatedPrice);

        if (request.getReceiverUserId() != null) {
            userRepository.findById(request.getReceiverUserId()).ifPresent(cargo::setReceiver);
        }

        Cargo savedCargo = cargoRepository.save(cargo);
        return convertToResponse(savedCargo);
    }

    public List<CargoResponse> getAllCargos() {
        return cargoRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CargoDetailResponse getCargoDetailsByTrackingNumber(String trackingNumber) {
        Cargo cargo = cargoRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Bu takip koduna ait bir kargo bulunamadı: " + trackingNumber));
        return convertToDetailResponse(cargo);
    }

    private Double calculateDistanceAndPrice(String originAddress, String destinationAddress) {

        double[] startCoords = openRouteService.getCoordinates(originAddress);
        double[] endCoords = openRouteService.getCoordinates(destinationAddress);
        double distanceKm = openRouteService.getDistanceInKm(startCoords, endCoords);

        double basePrice = systemSettingRepository.findById("CARGO_BASE_PRICE")
                .map(setting -> Double.parseDouble(setting.getValue()))
                .orElseThrow(() -> new RuntimeException("Sistem ayarları okunamıyor. Lütfen daha sonra tekrar deneyiniz."));

        double pricePerKm = systemSettingRepository.findById("CARGO_PRICE_PER_KM")
                .map(setting -> Double.parseDouble(setting.getValue()))
                .orElseThrow(() -> new RuntimeException("Sistem ayarları okunamıyor. Lütfen daha sonra tekrar deneyiniz."));

        double finalPrice = basePrice + (distanceKm * pricePerKm);

        return Math.round(finalPrice * 100.0) / 100.0;
    }

    private CargoResponse convertToResponse(Cargo cargo) {
        CargoResponse response = new CargoResponse();
        response.setId(cargo.getId());
        response.setTrackingNumber(cargo.getTrackingNumber());
        response.setSenderUsername(cargo.getSender().getUsername());
        response.setReceiverName(cargo.getReceiverName());
        response.setReceiverPhone(cargo.getReceiverPhone());
        response.setDeliveryAddress(cargo.getDeliveryAddress());
        response.setCurrentStatus(cargo.getCurrentStatus());
        response.setPickupTime(cargo.getPickupTime());
        response.setEstimatedPrice(cargo.getEstimatedPrice());
        response.setCurrentBranchName(cargo.getCurrentBranch().getName());

        if (cargo.getAssignedCourier() != null) {
            response.setAssignedCourierName(cargo.getAssignedCourier().getFirstName() + " " + cargo.getAssignedCourier().getLastName());
        }
        return response;
    }

    private CargoDetailResponse convertToDetailResponse(Cargo cargo) {
        CargoDetailResponse response = new CargoDetailResponse();
        response.setId(cargo.getId());
        response.setTrackingNumber(cargo.getTrackingNumber());
        response.setSenderUsername(cargo.getSender().getUsername());
        response.setReceiverName(cargo.getReceiverName());
        response.setReceiverPhone(cargo.getReceiverPhone());
        response.setDeliveryAddress(cargo.getDeliveryAddress());
        response.setCurrentStatus(cargo.getCurrentStatus());
        response.setPickupTime(cargo.getPickupTime());
        response.setEstimatedPrice(cargo.getEstimatedPrice());
        response.setCurrentBranchName(cargo.getCurrentBranch().getName());

        return response;
    }

    public Double calculatePriceForVisitor(String origin, String destination) {
        return calculateDistanceAndPrice(origin, destination);
    }
}