package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.ClusterRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.ClusterResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Cluster;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.ClusterMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.ClusterRepository;

@Service
public class ClusterService {

	@Autowired
	private ClusterRepository clusterRepository;

	@Autowired
	private ClusterMapper clusterMapper;

	public ClusterResponseDto cadastrarCluster(ClusterRequestDto request) {
		Cluster novoCluster = new Cluster();
		novoCluster.setNome(request.getNome());

		clusterRepository.save(novoCluster);

		return clusterMapper.montarDtoResposta(novoCluster);
	}

	public ClusterResponseDto buscarClusterPorId(Long idCluster) {
		var clusterFound = buscarClusterEntity(idCluster);
		return clusterMapper.montarDtoResposta(clusterFound);
	}

	public Cluster buscarClusterEntity(Long idCluster) {
		return clusterRepository.findById(idCluster)
				.orElseThrow(() -> new NaoEncontradoException("Cluster não encontrado."));
	}

}
