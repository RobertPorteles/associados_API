package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;   
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

import com.br.robertmiler.gerenciamento.domain.dtos.request.LoginRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RegisterRequest;
import com.br.robertmiler.gerenciamento.domain.dtos.response.LoginResponseDTO;
import com.br.robertmiler.gerenciamento.domain.dtos.response.RegisterResponse;
import com.br.robertmiler.gerenciamento.domain.service.auth.AuthService;
import com.br.robertmiler.gerenciamento.domain.service.auth.RegisterService;

import jakarta.validation.Valid;

@RestController             
@RequestMapping("/api/v1/auth")
public class AuthController { // ✅ typo corrigido

    @Autowired
    private AuthService authService;

    @Autowired
    private RegisterService registerService;
    // ✅ público — sem @PreAuthorize
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDto request) {
        LoginResponseDTO response = authService.autenticar(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADM')")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = registerService.registrar(request);
        return ResponseEntity.status(201).body(response);
    }
}
