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

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadosController {

	@Autowired
	private AssociadoService associadoService;

	@PostMapping
	public ResponseEntity<AssociadoResponseDto> postCadastrarAssociado(@Valid @RequestBody AssociadoRequestDto request, AssociadoEnderecoResidencialRequestDto enderecoResquest){
		var response = associadoService.cadastrarAssociado(request, enderecoResquest);
		return ResponseEntity.status(201).body(response);
	}

	@PutMapping("/{idAssociado}")
	public ResponseEntity<AssociadoResponseDto> putEditarAssociado(@PathVariable Long idAssociado, @Valid @RequestBody AssociadoRequestDto request) {
		var response = associadoService.editarAssociado(idAssociado, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<PaginacaoResponseDto<AssociadoResponseDto>> getTodosAssociados(
			@RequestParam(defaultValue = "0") Integer number,
			@RequestParam(defaultValue = "10") Integer size) {
		var response = associadoService.buscarTodosAssociados(number, size);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{idAssociado}")
	public ResponseEntity<AssociadoResponseDto> getAssociadoPorId(@PathVariable Long idAssociado) {
		var response = associadoService.buscarAssociadoPorId(idAssociado);
		return ResponseEntity.ok(response);
	}
}
