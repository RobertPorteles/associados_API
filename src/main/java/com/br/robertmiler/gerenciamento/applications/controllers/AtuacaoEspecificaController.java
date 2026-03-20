package com.br.robertmiler.gerenciamento.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AtuacaoEspecificaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AtuacaoEspecificaResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.AtuacaoEspecificaService;

@RestController
@RequestMapping("/api/v1/atuacoes-especificas")
public class AtuacaoEspecificaController {

	@Autowired
	private AtuacaoEspecificaService atuacaoEspecificaService;

	@PostMapping
	public ResponseEntity<AtuacaoEspecificaResponseDto> postCadastrarAtuacaoEspecifica(
			@RequestBody AtuacaoEspecificaRequestDto request) {
		var response = atuacaoEspecificaService.cadastrarAtuacaoEspecifica(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{idAtuacaoEspecifica}")
	public ResponseEntity<AtuacaoEspecificaResponseDto> getAtuacaoEspecificaPorId(
			@PathVariable Long idAtuacaoEspecifica) {
		var response = atuacaoEspecificaService.buscarAtuacaoEspecificaPorId(idAtuacaoEspecifica);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{idAtuacaoEspecifica}")
	public ResponseEntity<?> putEditarAtuacaoEspecifica(@PathVariable Long idAtuacaoEspecifica,
			@RequestBody AtuacaoEspecificaRequestDto request) {
		var response = atuacaoEspecificaService.editarAtuacaoEspecifica(idAtuacaoEspecifica, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cluster/{idCluster}")
	public ResponseEntity<PaginacaoResponseDto<AtuacaoEspecificaResponseDto>> getAtuacoesEspecificasPorCluster(
			@PathVariable Long idCluster,
			@RequestParam(defaultValue = "0") Integer number,
			@RequestParam(defaultValue = "20") Integer size) {
		var response = atuacaoEspecificaService.buscarAtuacoesEspecificasPorCluster(idCluster, number, size);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<PaginacaoResponseDto<AtuacaoEspecificaResponseDto>> getTodasAtuacoesEspecificas(
			@RequestParam(defaultValue = "0") Integer number,
			@RequestParam(defaultValue = "20") Integer size) {
		var response = atuacaoEspecificaService.buscarTodasAtuacoesEspecificas(number, size);
		return ResponseEntity.ok(response);
	}

}
