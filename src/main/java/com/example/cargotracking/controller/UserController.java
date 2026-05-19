package com.example.cargotracking.controller;

import com.example.cargotracking.dto.request.EmailUpdateRequest;
import com.example.cargotracking.dto.request.PasswordUpdateRequest;
import com.example.cargotracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(
            Authentication authentication,
            @RequestBody PasswordUpdateRequest request) {

        String username = authentication.getName();
        userService.updatePassword(username, request);

        return ResponseEntity.ok("Şifreniz başarıyla güncellendi.");
    }

    @PutMapping("/email")
    public ResponseEntity<String> updateEmail(
            Authentication authentication,
            @RequestBody EmailUpdateRequest request) {

        String username = authentication.getName();
        userService.updateEmail(username, request);

        return ResponseEntity.ok("E-posta adresiniz başarıyla güncellendi.");
    }

    @DeleteMapping("/account")
    public ResponseEntity<String> deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUser(username);

        return ResponseEntity.ok("Hesabınız başarıyla kapatıldı ve verileriniz arşivlendi.");
    }
}