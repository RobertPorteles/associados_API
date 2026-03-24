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

import com.br.robertmiler.gerenciamento.domain.dtos.request.ClusterRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.ClusterResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.service.ClusterService;

@RestController
@RequestMapping("/api/v1/clusters")
public class ClusterController {

	@Autowired
	private ClusterService clusterService;

	@PostMapping
	public ResponseEntity<ClusterResponseDto> postCadastrarCluster(@Valid @RequestBody ClusterRequestDto request) {
		var response = clusterService.cadastrarCluster(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping("/{idCluster}")
	public ResponseEntity<ClusterResponseDto> getClusterPorId(@PathVariable Long idCluster) {
		var response = clusterService.buscarClusterPorId(idCluster);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{idCluster}")
	public ResponseEntity<?> putEditarCluster(@PathVariable Long idCluster, @RequestBody ClusterRequestDto request) {
		var response = clusterService.editarCluster(idCluster, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<PaginacaoResponseDto<ClusterResponseDto>> getTodosClusters(
			@RequestParam(defaultValue = "0") Integer number,
			@RequestParam(defaultValue = "20") Integer size) {
		var response = clusterService.buscarTodosClusters(number, size);
		return ResponseEntity.ok(response);
	}

}
