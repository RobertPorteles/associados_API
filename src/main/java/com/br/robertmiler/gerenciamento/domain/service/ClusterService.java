package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.ClusterRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.ClusterResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Cluster;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.ClusterMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.ClusterRepository;


@Service
public class ClusterService {

	@Autowired
	private ClusterRepository clusterRepository;

	@Autowired
	private ClusterMapper clusterMapper;

	@Autowired
	private PaginacaoMapper paginacaoMapper;

	@Transactional
	public ClusterResponseDto cadastrarCluster(ClusterRequestDto request) {

		clusterRepository.findByNome(FormataString.primeiraLetraMaiuscula(request.getNome()))
				.ifPresent(c -> {
					throw new RegraNegocioException("Este nome de cluster já existe.");
				});

		Cluster novoCluster = clusterMapper.toEntity(request);
		clusterRepository.save(novoCluster);

		return clusterMapper.toResponse(novoCluster);
	}

	@Transactional
	public ClusterResponseDto editarCluster(Long idCluster, ClusterRequestDto request) {

		var clusterFound = buscarClusterEntity(idCluster);

		String nome = FormataString.primeiraLetraMaiuscula(request.getNome());

		clusterRepository.findByNome(nome)
            .filter(c -> !c.getIdCluster().equals(idCluster))
            .ifPresent(c -> {
                throw new RegraNegocioException("Este nome de cluster já existe.");
            });

		clusterFound.setNome(nome);
		clusterRepository.save(clusterFound);

		return clusterMapper.toResponse(clusterFound);
	}

	@Transactional(readOnly = true)
	public ClusterResponseDto buscarClusterPorId(Long idCluster) {
		var clusterFound = buscarClusterEntity(idCluster);

		return clusterMapper.toResponse(clusterFound);
	}

	@Transactional(readOnly = true)
	public Cluster buscarClusterEntity(Long idCluster) {
		return clusterRepository.findById(idCluster)
				.orElseThrow(() -> new NaoEncontradoException("Cluster não encontrado."));
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<ClusterResponseDto> buscarTodosClusters(Integer number, Integer size) {

		var pageable = PageRequest.of(number, size, Sort.by("nome").ascending());

		var paginaFound = clusterRepository.findAll(pageable);

		var pageResponse = paginaFound.map(clusterMapper::toResponse);

		return paginacaoMapper.montarDtoResposta(pageResponse);
	}

}
