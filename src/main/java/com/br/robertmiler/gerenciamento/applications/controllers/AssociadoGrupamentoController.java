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

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoGrupamentoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoGrupamentoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoGrupamentoService;

@RestController
@RequestMapping("/api/v1/associados-grupamentos")
public class AssociadoGrupamentoController {

    @Autowired
    private AssociadoGrupamentoService associadoGrupamentoService;

    @PostMapping
    public ResponseEntity<AssociadoGrupamentoResponseDto> postVincularGrupamento(@RequestBody AssociadoGrupamentoRequestDto request) {
        var response = associadoGrupamentoService.vincularGrupamento(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idAssociadoGrupamento}")
    public ResponseEntity<AssociadoGrupamentoResponseDto> getVinculoPorId(@PathVariable Long idAssociadoGrupamento) {
        var response = associadoGrupamentoService.buscarVinculoPorId(idAssociadoGrupamento);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idAssociadoGrupamento}")
    public ResponseEntity<?> putEditarVinculo(@PathVariable Long idAssociadoGrupamento, @RequestBody AssociadoGrupamentoRequestDto request) {
        var response = associadoGrupamentoService.editarVinculo(idAssociadoGrupamento, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<List<AssociadoGrupamentoResponseDto>> getGrupamentosPorAssociado(@PathVariable Long idAssociado) {
        var response = associadoGrupamentoService.buscarGrupamentosPorAssociado(idAssociado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grupamento/{idGrupamento}")
    public ResponseEntity<List<AssociadoGrupamentoResponseDto>> getAssociadosPorGrupamento(@PathVariable Long idGrupamento) {
        var response = associadoGrupamentoService.buscarAssociadosPorGrupamento(idGrupamento);
        return ResponseEntity.ok(response);
    }

}
