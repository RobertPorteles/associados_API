package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadosController {
	
	@Autowired
	private AssociadoService associadoService;

	@PostMapping
	public ResponseEntity<AssociadoResponseDto> postCadastrarAssociado(@RequestBody AssociadoRequestDto request){
		var response = associadoService.cadastrarAssociado(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{idAssociado}")
	public ResponseEntity<AssociadoResponseDto> getAssociadoPorId(@PathVariable Long idAssociado) {
		var response = associadoService.buscarAssociadoPorId(idAssociado);
		return ResponseEntity.ok(response);
	}
}
