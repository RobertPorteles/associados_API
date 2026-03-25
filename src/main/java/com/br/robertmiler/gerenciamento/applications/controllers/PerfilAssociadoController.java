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

import com.br.robertmiler.gerenciamento.domain.dtos.request.PerfilAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PerfilAssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.PerfilAssociadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/perfis")
public class PerfilAssociadoController {

    @Autowired
    private PerfilAssociadoService perfilService;

    @PostMapping
    public ResponseEntity<PerfilAssociadoResponseDto> postCadastrarPerfil(
            @RequestBody @Valid PerfilAssociadoRequestDto request) {
        var response = perfilService.cadastrarPerfil(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idPerfil}")
    public ResponseEntity<PerfilAssociadoResponseDto> getPerfilPorId(@PathVariable Long idPerfil) {
        var response = perfilService.buscarPerfilPorId(idPerfil);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<PerfilAssociadoResponseDto> getPerfilPorAssociado(
            @PathVariable Long idAssociado) {
        var response = perfilService.buscarPerfilPorAssociado(idAssociado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idPerfil}")
    public ResponseEntity<?> putEditarPerfil(
            @PathVariable Long idPerfil,
            @RequestBody @Valid PerfilAssociadoRequestDto request) {
        var response = perfilService.editarPerfil(idPerfil, request);
        return ResponseEntity.ok(response);
    }

}
