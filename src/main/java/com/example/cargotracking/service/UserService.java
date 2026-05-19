package com.example.cargotracking.service;

import com.example.cargotracking.dto.request.EmailUpdateRequest;
import com.example.cargotracking.dto.request.PasswordUpdateRequest;
import com.example.cargotracking.entity.User;
import com.example.cargotracking.exception.BusinessException;
import com.example.cargotracking.exception.ResourceNotFoundException;
import com.example.cargotracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updatePassword(String username, PasswordUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı."));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("Girdiğiniz mevcut şifre hatalı.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);

        userRepository.save(user);
    }

    public void updateEmail(String username, EmailUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı."));

        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten başka bir hesaba kayıtlı.");
        }

        user.setEmail(request.getNewEmail());

        userRepository.save(user);
    }
}