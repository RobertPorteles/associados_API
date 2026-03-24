package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeLocalPresencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeLocalPresencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipeLocalPresencialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/locais-presenciais")
public class EquipeLocalPresencialController {

    @Autowired
    private EquipeLocalPresencialService localPresencialService;

    @PostMapping
    public ResponseEntity<EquipeLocalPresencialResponseDto> postCadastrarLocalPresencial(
            @RequestBody @Valid EquipeLocalPresencialRequestDto request) {
        var response = localPresencialService.cadastrarLocalPresencial(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idLocalPresencial}")
    public ResponseEntity<EquipeLocalPresencialResponseDto> getLocalPresencialPorId(
            @PathVariable Long idLocalPresencial) {
        var response = localPresencialService.buscarLocalPresencialPorId(idLocalPresencial);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipe/{idEquipe}")
    public ResponseEntity<EquipeLocalPresencialResponseDto> getLocalPresencialPorEquipe(
            @PathVariable Long idEquipe) {
        var response = localPresencialService.buscarLocalPresencialPorEquipe(idEquipe);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idLocalPresencial}")
    public ResponseEntity<?> putEditarLocalPresencial(
            @PathVariable Long idLocalPresencial,
            @RequestBody @Valid EquipeLocalPresencialRequestDto request) {
        var response = localPresencialService.editarLocalPresencial(idLocalPresencial, request);
        return ResponseEntity.ok(response);
    }

}
