package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.GrupamentoEstrategicoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.GrupamentoEstrategicoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.GrupamentoEstrategicoService;

@RestController
@RequestMapping("/api/v1/grupamentos")
public class GrupamentoEstrategicoController {

    @Autowired
    private GrupamentoEstrategicoService grupamentoService;

    @PostMapping
    public ResponseEntity<GrupamentoEstrategicoResponseDto> postCadastrarGrupamento(@Valid @RequestBody GrupamentoEstrategicoRequestDto request) {
        var response = grupamentoService.cadastrarGrupamento(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idGrupamento}")
    public ResponseEntity<GrupamentoEstrategicoResponseDto> getGrupamentoPorId(@PathVariable Long idGrupamento) {
        var response = grupamentoService.buscarGrupamentoPorId(idGrupamento);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idGrupamento}")
    public ResponseEntity<?> putEditarGrupamento(@PathVariable Long idGrupamento, @RequestBody GrupamentoEstrategicoRequestDto request) {
        var response = grupamentoService.editarGrupamento(idGrupamento, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponseDto<GrupamentoEstrategicoResponseDto>> getTodosGrupamentos(
            @RequestParam(defaultValue = "0") Integer number,
            @RequestParam(defaultValue = "20") Integer size) {
        var response = grupamentoService.buscarTodosGrupamentos(number, size);
        return ResponseEntity.ok(response);
    }

}
