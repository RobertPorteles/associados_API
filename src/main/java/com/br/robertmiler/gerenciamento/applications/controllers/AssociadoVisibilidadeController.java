package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoVisibilidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoVisibilidadeResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoVisibilidadeService;

/**
 * Visibilidade é criada automaticamente pelo AssociadoService no cadastro do associado.
 * Este controller expõe apenas consulta e edição das preferências.
 */
@RestController
@RequestMapping("/api/v1/visibilidades")
public class AssociadoVisibilidadeController {

    @Autowired
    private AssociadoVisibilidadeService associadoVisibilidadeService;

    @GetMapping("/{idVisibilidade}")
    public ResponseEntity<AssociadoVisibilidadeResponseDto> getVisibilidadePorId(
            @PathVariable Long idVisibilidade) {
        var response = associadoVisibilidadeService.buscarVisibilidadePorId(idVisibilidade);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<AssociadoVisibilidadeResponseDto> getVisibilidadePorAssociado(
            @PathVariable Long idAssociado) {
        var response = associadoVisibilidadeService.buscarVisibilidadePorAssociado(idAssociado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idVisibilidade}")
    public ResponseEntity<?> putEditarVisibilidade(
            @PathVariable Long idVisibilidade,
            @RequestBody AssociadoVisibilidadeRequestDto request) {
        var response = associadoVisibilidadeService.editarVisibilidade(idVisibilidade, request);
        return ResponseEntity.ok(response);
    }

}
