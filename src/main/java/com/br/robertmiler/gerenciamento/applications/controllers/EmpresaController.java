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

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.EmpresaService;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@PostMapping
	public ResponseEntity<EmpresaResponseDto> postCadastrarEmpresa(@RequestBody EmpresaRequestDto request) {
		var response = empresaService.criar(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{idEmpresa}")
	public ResponseEntity<EmpresaResponseDto> getEmpresaPorId(@PathVariable Long idEmpresa) {
		var response = empresaService.buscarEmpresaPorId(idEmpresa);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{idEmpresa}")
	public ResponseEntity<EmpresaResponseDto> putEditarEmpresa(@PathVariable Long idEmpresa, @RequestBody EmpresaRequestDto request) {
		var response = empresaService.editarEmpresa(idEmpresa, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/associado/{idAssociado}")
	public ResponseEntity<PaginacaoResponseDto<EmpresaResponseDto>> getEmpresasPorAssociado(
			@PathVariable Long idAssociado,
			@RequestParam(defaultValue = "0") Integer number,
			@RequestParam(defaultValue = "10") Integer size) {
		var response = empresaService.buscarEmpresasPorAssociado(idAssociado, number, size);
		return ResponseEntity.ok(response);
	}

}
