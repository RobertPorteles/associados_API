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

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDiretorTerritorioRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDiretorTerritorioResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipeDiretorTerritorioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/diretores-territorio")
public class EquipeDiretorTerritorioController {

    @Autowired
    private EquipeDiretorTerritorioService diretorTerritorioService;

    @PostMapping
    public ResponseEntity<EquipeDiretorTerritorioResponseDto> postCadastrarDiretorTerritorio(
            @RequestBody @Valid EquipeDiretorTerritorioRequestDto request) {
        var response = diretorTerritorioService.cadastrarDiretorTerritorio(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idDiretorTerritorio}")
    public ResponseEntity<EquipeDiretorTerritorioResponseDto> getDiretorTerritorioPorId(
            @PathVariable Long idDiretorTerritorio) {
        var response = diretorTerritorioService.buscarDiretorTerritorioPorId(idDiretorTerritorio);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipe/{idEquipe}")
    public ResponseEntity<List<EquipeDiretorTerritorioResponseDto>> getDiretoresTerritorioPorEquipe(
            @PathVariable Long idEquipe) {
        var response = diretorTerritorioService.buscarDiretoresTerritorioPorEquipe(idEquipe);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idDiretorTerritorio}")
    public ResponseEntity<?> putEditarDiretorTerritorio(
            @PathVariable Long idDiretorTerritorio,
            @RequestBody @Valid EquipeDiretorTerritorioRequestDto request) {
        var response = diretorTerritorioService.editarDiretorTerritorio(idDiretorTerritorio, request);
        return ResponseEntity.ok(response);
    }

}
