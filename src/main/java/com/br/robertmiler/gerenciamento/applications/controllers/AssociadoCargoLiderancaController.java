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

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoCargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoCargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoCargoLiderancaService;

@RestController
@RequestMapping("/api/v1/associados-cargos")
public class AssociadoCargoLiderancaController {

    @Autowired
    private AssociadoCargoLiderancaService associadoCargoService;

    @PostMapping
    public ResponseEntity<AssociadoCargoLiderancaResponseDto> postDesignarCargo(@RequestBody AssociadoCargoLiderancaRequestDto request) {
        var response = associadoCargoService.designarCargo(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idAssociadoCargo}")
    public ResponseEntity<AssociadoCargoLiderancaResponseDto> getDesignacaoPorId(@PathVariable Long idAssociadoCargo) {
        var response = associadoCargoService.buscarDesignacaoPorId(idAssociadoCargo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idAssociadoCargo}")
    public ResponseEntity<?> putEditarDesignacao(@PathVariable Long idAssociadoCargo, @RequestBody AssociadoCargoLiderancaRequestDto request) {
        var response = associadoCargoService.editarDesignacao(idAssociadoCargo, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<List<AssociadoCargoLiderancaResponseDto>> getCargosPorAssociado(@PathVariable Long idAssociado) {
        var response = associadoCargoService.buscarCargosPorAssociado(idAssociado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cargo/{idCargoLideranca}")
    public ResponseEntity<List<AssociadoCargoLiderancaResponseDto>> getAssociadosPorCargo(@PathVariable Long idCargoLideranca) {
        var response = associadoCargoService.buscarAssociadosPorCargo(idCargoLideranca);
        return ResponseEntity.ok(response);
    }

}
