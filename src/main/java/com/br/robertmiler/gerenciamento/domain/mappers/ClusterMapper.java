package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.ClusterResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Cluster;

@Component
public class ClusterMapper {

	public ClusterResponseDto montarDtoResposta(Cluster cluster) {
		ClusterResponseDto response = new ClusterResponseDto();
		response.setIdCluster(cluster.getIdCluster());
		response.setNome(cluster.getNome());
		return response;
	}

}
