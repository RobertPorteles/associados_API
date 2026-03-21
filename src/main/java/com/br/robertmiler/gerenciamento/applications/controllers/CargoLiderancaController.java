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

import com.br.robertmiler.gerenciamento.domain.dtos.request.CargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.CargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.CargoLiderancaService;

@RestController
@RequestMapping("/api/v1/cargos-lideranca")
public class CargoLiderancaController {

    @Autowired
    private CargoLiderancaService cargoLiderancaService;

    @PostMapping
    public ResponseEntity<CargoLiderancaResponseDto> postCadastrarCargo(@Valid @RequestBody CargoLiderancaRequestDto request) {
        var response = cargoLiderancaService.cadastrarCargo(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{idCargoLideranca}")
    public ResponseEntity<CargoLiderancaResponseDto> getCargoPorId(@PathVariable Long idCargoLideranca) {
        var response = cargoLiderancaService.buscarCargoPorId(idCargoLideranca);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idCargoLideranca}")
    public ResponseEntity<?> putEditarCargo(@PathVariable Long idCargoLideranca, @RequestBody CargoLiderancaRequestDto request) {
        var response = cargoLiderancaService.editarCargo(idCargoLideranca, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponseDto<CargoLiderancaResponseDto>> getTodosCargos(
            @RequestParam(defaultValue = "0") Integer number,
            @RequestParam(defaultValue = "20") Integer size) {
        var response = cargoLiderancaService.buscarTodosCargos(number, size);
        return ResponseEntity.ok(response);
    }

}
