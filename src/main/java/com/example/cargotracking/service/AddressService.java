package com.example.cargotracking.service;

import com.example.cargotracking.dto.request.AddressCreateRequest;
import com.example.cargotracking.dto.response.AddressResponse;
import com.example.cargotracking.entity.Address;
import com.example.cargotracking.entity.User;
import com.example.cargotracking.exception.BusinessException;
import com.example.cargotracking.exception.ResourceNotFoundException;
import com.example.cargotracking.repository.AddressRepository;
import com.example.cargotracking.repository.UserRepository; // User verisine erişmek için eklendi
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressResponse createAddress(AddressCreateRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Oturum açmış kullanıcı veritabanında bulunamadı!"));

        Address address = new Address();
        address.setAddressTitle(request.getAddressTitle());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setFullAddress(request.getFullAddress());
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return convertToResponse(savedAddress);
    }

    public void deleteAddress(Long addressId, String username) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Adres bulunamadı! ID: " + addressId));

        if (!address.getUser().getUsername().equals(username)) {
            throw new BusinessException("Bu adresi silme yetkiniz bulunmamaktadır.");
        }

        addressRepository.delete(address);
    }

    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AddressResponse getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adres bulunamadı! ID: " + id));
        return convertToResponse(address);
    }

    private AddressResponse convertToResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setAddressTitle(address.getAddressTitle());
        response.setCity(address.getCity());
        response.setDistrict(address.getDistrict());
        response.setFullAddress(address.getFullAddress());

        if (address.getUser() != null) {
            response.setUserId(address.getUser().getId());
        }

        return response;
    }
}