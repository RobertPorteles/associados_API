package com.br.robertmiler.gerenciamento.applications.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDiretorEquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDiretorEquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipeDiretorEquipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/diretores-equipe")
public class EquipeDiretorEquipeController {

    @Autowired
    private EquipeDiretorEquipeService diretorEquipeService;

    @PostMapping
    public ResponseEntity<EquipeDiretorEquipeResponseDto> postCadastrarDiretorEquipe(
            @RequestBody @Valid EquipeDiretorEquipeRequestDto request) {
        var response = diretorEquipeService.cadastrarDiretorEquipe(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idDiretorEquipe}")
    public ResponseEntity<EquipeDiretorEquipeResponseDto> getDiretorEquipePorId(
            @PathVariable Long idDiretorEquipe) {
        var response = diretorEquipeService.buscarDiretorEquipePorId(idDiretorEquipe);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipe/{idEquipe}")
    public ResponseEntity<List<EquipeDiretorEquipeResponseDto>> getDiretoresEquipePorEquipe(
            @PathVariable Long idEquipe) {
        var response = diretorEquipeService.buscarDiretoresEquipePorEquipe(idEquipe);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idDiretorEquipe}")
    public ResponseEntity<?> putEditarDiretorEquipe(
            @PathVariable Long idDiretorEquipe,
            @RequestBody @Valid EquipeDiretorEquipeRequestDto request) {
        var response = diretorEquipeService.editarDiretorEquipe(idDiretorEquipe, request);
        return ResponseEntity.ok(response);
    }

}
