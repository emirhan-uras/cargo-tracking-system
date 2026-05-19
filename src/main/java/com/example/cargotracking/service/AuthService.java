package com.example.cargotracking.service;

import com.example.cargotracking.config.JwtUtil;
import com.example.cargotracking.dto.request.LoginRequest;
import com.example.cargotracking.dto.request.RegisterRequest;
import com.example.cargotracking.dto.response.AuthResponse;
import com.example.cargotracking.entity.User;
import com.example.cargotracking.entity.enums.Role;
import com.example.cargotracking.exception.BusinessException;
import com.example.cargotracking.exception.ResourceNotFoundException;
import com.example.cargotracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Bu kullanıcı adı zaten alınmış!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten kullanımda!");
        }


        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        user.setRole(Role.ROLE_CUSTOMER);

        userRepository.save(user);
        return "Kullanıcı başarıyla kaydedildi.";
    }


    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı!"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}