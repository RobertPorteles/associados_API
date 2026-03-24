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

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipePontuacaoFaixaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipePontuacaoFaixaResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipePontuacaoFaixaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pontuacao-faixas")
public class EquipePontuacaoFaixaController {

    @Autowired
    private EquipePontuacaoFaixaService pontuacaoFaixaService;

    @PostMapping
    public ResponseEntity<EquipePontuacaoFaixaResponseDto> postCadastrarFaixa(
            @RequestBody @Valid EquipePontuacaoFaixaRequestDto request) {
        var response = pontuacaoFaixaService.cadastrarFaixa(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idFaixa}")
    public ResponseEntity<EquipePontuacaoFaixaResponseDto> getFaixaPorId(@PathVariable Long idFaixa) {
        var response = pontuacaoFaixaService.buscarFaixaPorId(idFaixa);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EquipePontuacaoFaixaResponseDto>> getTodasFaixas() {
        var response = pontuacaoFaixaService.buscarTodasFaixas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<EquipePontuacaoFaixaResponseDto>> getFaixasAtivas() {
        var response = pontuacaoFaixaService.buscarFaixasAtivas();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idFaixa}")
    public ResponseEntity<?> putEditarFaixa(
            @PathVariable Long idFaixa,
            @RequestBody @Valid EquipePontuacaoFaixaRequestDto request) {
        var response = pontuacaoFaixaService.editarFaixa(idFaixa, request);
        return ResponseEntity.ok(response);
    }

}
