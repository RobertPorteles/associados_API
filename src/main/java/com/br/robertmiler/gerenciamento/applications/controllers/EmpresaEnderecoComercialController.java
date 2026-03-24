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
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaEnderecoComercialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaEnderecoComercialResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EmpresaEnderecoComercialService;

@RestController
@RequestMapping("/api/v1/enderecos-comerciais")
public class EmpresaEnderecoComercialController {

    @Autowired
    private EmpresaEnderecoComercialService enderecoComercialService;

    @PostMapping
    public ResponseEntity<EmpresaEnderecoComercialResponseDto> postCadastrarEnderecoComercial(
            @Valid @RequestBody EmpresaEnderecoComercialRequestDto request) {
        var response = enderecoComercialService.cadastrarEnderecoComercial(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{idEnderecoComercial}")
    public ResponseEntity<?> putEditarEnderecoComercial(@PathVariable Long idEnderecoComercial,
            @Valid @RequestBody EmpresaEnderecoComercialRequestDto request) {
        var response = enderecoComercialService.editarEnderecoComercial(idEnderecoComercial, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<EmpresaEnderecoComercialResponseDto> getEnderecoComercialPorEmpresa(
            @PathVariable Long idEmpresa) {
        var response = enderecoComercialService.buscarEnderecoComercialPorEmpresa(idEmpresa);
        return ResponseEntity.ok(response);
    }

}
