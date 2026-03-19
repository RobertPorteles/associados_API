package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EquipeService;

@RestController
@RequestMapping("/api/v1/equipes")
public class EquipesController {

	@Autowired
	private EquipeService equipeService;

	@PostMapping("/cadastrar")
	public ResponseEntity<EquipeResponseDto> postCadastrarEquipe(@RequestBody EquipeRequestDto request) {
		var response = equipeService.cadastrarEquipe(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{idEquipe}")
	public ResponseEntity<EquipeResponseDto> getEquipePorId(@PathVariable Long idEquipe) {
		var response = equipeService.buscarEquipePorId(idEquipe);
		return ResponseEntity.ok(response);
	}
}
