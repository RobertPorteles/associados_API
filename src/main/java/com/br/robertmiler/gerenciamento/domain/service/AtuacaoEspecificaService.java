package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AtuacaoEspecificaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AtuacaoEspecificaResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AtuacaoEspecifica;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.AtuacaoEspecificaMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AtuacaoEspecificaRepository;

@Service
public class AtuacaoEspecificaService {

	@Autowired
	private AtuacaoEspecificaRepository atuacaoEspecificaRepository;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private AtuacaoEspecificaMapper atuacaoEspecificaMapper;

	@Autowired
	private PaginacaoMapper paginacaoMapper;

	@Transactional
	public AtuacaoEspecificaResponseDto cadastrarAtuacaoEspecifica(AtuacaoEspecificaRequestDto request) {

		var clusterFound = clusterService.buscarClusterEntity(request.getIdCluster());

		String nome = FormataString.primeiraLetraMaiuscula(request.getNome());

		atuacaoEspecificaRepository.findByNomeAndCluster_IdCluster(nome, clusterFound.getIdCluster())
				.ifPresent(a -> {
					throw new RegraNegocioException(
							"Já existe uma atuação específica com este nome para o cluster informado.");
				});

		var novaAtuacao = atuacaoEspecificaMapper.toEntity(request);
		novaAtuacao.setCluster(clusterFound);

		atuacaoEspecificaRepository.save(novaAtuacao);

		return atuacaoEspecificaMapper.toResponse(novaAtuacao);
	}

	@Transactional
	public AtuacaoEspecificaResponseDto editarAtuacaoEspecifica(Long idAtuacaoEspecifica,
			AtuacaoEspecificaRequestDto request) {

		var atuacaoFound = buscarAtuacaoEspecificaEntity(idAtuacaoEspecifica);

		var clusterFound = clusterService.buscarClusterEntity(request.getIdCluster());

		String nome = FormataString.primeiraLetraMaiuscula(request.getNome());

		atuacaoEspecificaRepository.findByNomeAndCluster_IdCluster(nome, clusterFound.getIdCluster())
				.ifPresent(existente -> {
					if (!existente.getId().equals(atuacaoFound.getId()))
						throw new RegraNegocioException(
								"Já existe uma atuação específica com este nome para o cluster informado.");
				});

		atuacaoFound.setNome(nome);
		atuacaoFound.setCluster(clusterFound);

		atuacaoEspecificaRepository.save(atuacaoFound);

		return atuacaoEspecificaMapper.toResponse(atuacaoFound);
	}

	@Transactional(readOnly = true)
	public AtuacaoEspecificaResponseDto buscarAtuacaoEspecificaPorId(Long idAtuacaoEspecifica) {
		var atuacaoFound = buscarAtuacaoEspecificaEntity(idAtuacaoEspecifica);

		return atuacaoEspecificaMapper.toResponse(atuacaoFound);
	}

	@Transactional(readOnly = true)
	public AtuacaoEspecifica buscarAtuacaoEspecificaEntity(Long idAtuacaoEspecifica) {
		return atuacaoEspecificaRepository.findById(idAtuacaoEspecifica)
				.orElseThrow(() -> new NaoEncontradoException("Atuação específica não encontrada."));
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<AtuacaoEspecificaResponseDto> buscarAtuacoesEspecificasPorCluster(Long idCluster,
			Integer number, Integer size) {

		clusterService.buscarClusterEntity(idCluster);

		var pageable = PageRequest.of(number, size, Sort.by("nome").ascending());

		var paginaFound = atuacaoEspecificaRepository.findByCluster_IdCluster(idCluster, pageable);

		var pageResponse = paginaFound.map(atuacaoEspecificaMapper::toResponse);

		return paginacaoMapper.montarDtoResposta(pageResponse);
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<AtuacaoEspecificaResponseDto> buscarTodasAtuacoesEspecificas(Integer number,
			Integer size) {

		var pageable = PageRequest.of(number, size, Sort.by("nome").ascending());

		var paginaFound = atuacaoEspecificaRepository.findAll(pageable);

		var pageResponse = paginaFound.map(atuacaoEspecificaMapper::toResponse);

		return paginacaoMapper.montarDtoResposta(pageResponse);
	}

}
