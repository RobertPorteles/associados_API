package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AtuacaoEspecificaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AtuacaoEspecificaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AtuacaoEspecifica;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AtuacaoEspecificaMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AtuacaoEspecificaRepository;

@Service
public class AtuacaoEspecificaService {

	@Autowired
	private AtuacaoEspecificaRepository atuacaoEspecificaRepository;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private AtuacaoEspecificaMapper atuacaoEspecificaMapper;

	public AtuacaoEspecificaResponseDto cadastrarAtuacaoEspecifica(AtuacaoEspecificaRequestDto request) {
		var clusterFound = clusterService.buscarClusterEntity(request.getIdCluster());

		AtuacaoEspecifica novaAtuacao = new AtuacaoEspecifica();
		novaAtuacao.setNome(request.getNome());
		novaAtuacao.setCluster(clusterFound);

		atuacaoEspecificaRepository.save(novaAtuacao);

		return atuacaoEspecificaMapper.montarDtoResposta(novaAtuacao);
	}

	public AtuacaoEspecifica buscarAtuacaoEspecificaEntity(Long idAtuacaoEspecifica) {
		var atuacaoFound = atuacaoEspecificaRepository.findById(idAtuacaoEspecifica)
				.orElseThrow(() -> new NaoEncontradoException("Atuação específica não encontrada."));

		return atuacaoFound;
	}

}
