package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.ClusterRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.ClusterResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Cluster;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class ClusterMapper {

	public Cluster toEntity(ClusterRequestDto request) {
		Cluster novoCluster = new Cluster();
		novoCluster.setNome(FormataString.primeiraLetraMaiuscula(request.getNome()));

		return novoCluster;
	}

	public ClusterResponseDto toResponse(Cluster cluster) {
		ClusterResponseDto response = new ClusterResponseDto();
		response.setIdCluster(cluster.getIdCluster());
		response.setNome(cluster.getNome());

		return response;
	}

}
