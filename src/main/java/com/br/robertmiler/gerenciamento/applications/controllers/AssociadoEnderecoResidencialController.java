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

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoEnderecoResidencialResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoEnderecoResidencialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/enderecos-residenciais")
public class AssociadoEnderecoResidencialController {

    @Autowired
    private AssociadoEnderecoResidencialService enderecoResidencialService;

    // 1. Adicionamos o caminho na URL para receber o ID do associado
    @PostMapping("/associado/{idAssociado}")
    public ResponseEntity<AssociadoEnderecoResidencialResponseDto> postCadastrarEnderecoResidencial(
            @PathVariable Long idAssociado, // 2. Capturamos o ID da URL
            @Valid @RequestBody AssociadoEnderecoResidencialRequestDto request) {

        // 3. Passamos as duas informações reais para o Service (e colocamos o ponto e
        // vírgula!)
        var response = enderecoResidencialService.cadastrarEnderecoResidencial(idAssociado, request);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{idEndereco}")
    public ResponseEntity<?> putEditarEnderecoResidencial(@PathVariable Long idEndereco,
            @Valid @RequestBody AssociadoEnderecoResidencialRequestDto request) {
        var response = enderecoResidencialService.editarEnderecoResidencial(idEndereco, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<List<AssociadoEnderecoResidencialResponseDto>> getEnderecosResidenciaisPorAssociado(
            @PathVariable Long idAssociado) {
        var response = enderecoResidencialService.buscarEnderecosResidenciaisPorAssociado(idAssociado);
        return ResponseEntity.ok(response);
    }

}
