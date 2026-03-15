package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoEnderecoResidencialService;

@RestController
@RequestMapping("/api/v1/enderecos-residenciais")
public class AssociadoEnderecoResidencialController {

    @Autowired
    private AssociadoEnderecoResidencialService enderecoResidencialService;

    @PostMapping
    public ResponseEntity<AssociadoEnderecoResidencialResponseDto> postCadastrarEnderecoResidencial(
            @RequestBody AssociadoEnderecoResidencialRequestDto request) {
        var response = enderecoResidencialService.cadastrarEnderecoResidencial(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<AssociadoEnderecoResidencialResponseDto> getEnderecoResidencialPorAssociado(
            @PathVariable Long idAssociado) {
        var response = enderecoResidencialService.buscarEnderecoResidencialPorAssociado(idAssociado);
        return ResponseEntity.ok(response);
    }

}
