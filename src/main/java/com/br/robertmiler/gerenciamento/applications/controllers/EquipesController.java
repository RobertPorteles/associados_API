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

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipeService;

@RestController
@RequestMapping("/api/v1/equipes")
public class EquipesController {

	@Autowired
	private EquipeService equipeService;

	@PostMapping("/cadastrar")
	public ResponseEntity<EquipeResponseDto> postCadastrarEquipe(@Valid @RequestBody EquipeRequestDto request) {
		var response = equipeService.cadastrarEquipe(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{idEquipe}")
	public ResponseEntity<EquipeResponseDto> getEquipePorId(@PathVariable Long idEquipe) {
		var response = equipeService.buscarEquipePorId(idEquipe);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{idEquipe}")
	public ResponseEntity<?> putEditarEquipe(@PathVariable Long idEquipe, @RequestBody EquipeRequestDto request) {
		var response = equipeService.editarEquipe(idEquipe, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<PaginacaoResponseDto<EquipeResponseDto>> getTodasEquipes(
			@RequestParam(defaultValue = "0") Integer number,
			@RequestParam(defaultValue = "20") Integer size) {
		var response = equipeService.buscarTodasEquipes(number, size);
		return ResponseEntity.ok(response);
	}
}
