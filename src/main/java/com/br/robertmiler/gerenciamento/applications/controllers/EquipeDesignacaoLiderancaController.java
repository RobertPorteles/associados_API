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

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeDesignacaoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeDesignacaoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipeDesignacaoLiderancaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/designacoes-lideranca")
public class EquipeDesignacaoLiderancaController {

    @Autowired
    private EquipeDesignacaoLiderancaService designacaoService;

    @PostMapping
    public ResponseEntity<EquipeDesignacaoLiderancaResponseDto> postCadastrarDesignacao(
            @RequestBody @Valid EquipeDesignacaoLiderancaRequestDto request) {
        var response = designacaoService.cadastrarDesignacao(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idDesignacao}")
    public ResponseEntity<EquipeDesignacaoLiderancaResponseDto> getDesignacaoPorId(
            @PathVariable Long idDesignacao) {
        var response = designacaoService.buscarDesignacaoPorId(idDesignacao);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipe/{idEquipe}")
    public ResponseEntity<List<EquipeDesignacaoLiderancaResponseDto>> getDesignacoesPorEquipe(
            @PathVariable Long idEquipe) {
        var response = designacaoService.buscarDesignacoesPorEquipe(idEquipe);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idDesignacao}")
    public ResponseEntity<?> putEditarDesignacao(
            @PathVariable Long idDesignacao,
            @RequestBody @Valid EquipeDesignacaoLiderancaRequestDto request) {
        var response = designacaoService.editarDesignacao(idDesignacao, request);
        return ResponseEntity.ok(response);
    }

}
