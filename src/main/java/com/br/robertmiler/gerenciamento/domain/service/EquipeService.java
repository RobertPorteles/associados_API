package com.br.robertmiler.gerenciamento.domain.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Equipe;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.EquipeMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeRepository;

@Service
public class EquipeService {

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private EquipeMapper equipeMapper;

	@Autowired
	private PaginacaoMapper paginacaoMapper;

	@Transactional
	public EquipeResponseDto editarEquipe(Long idEquipe, EquipeRequestDto request) {

		var equipeFound = buscarEquipeEntity(idEquipe);

		var nome = FormataString.primeiraLetraMaiuscula(request.getNomeEquipe());

		equipeRepository.findByNomeEquipe(nome).ifPresent(e -> {
			if (!e.getIdEquipe().equals(idEquipe)) {
				throw new RegraNegocioException("Este nome de equipe já existe.");
			}
		});

		equipeFound.setNomeEquipe(nome);
		equipeFound.setDataInicioFormacao(request.getDataInicioFormacao());
		equipeFound.setDataPrevisaoLancamento(request.getDataInicioFormacao().plusDays(63));
		equipeFound.setDataEfetivaLancamento(request.getDataEfetivaLancamento());
		equipeFound.setDiaReuniao(request.getDiaReuniao());
		equipeFound.setHorarioReuniao(request.getHorarioReuniao());
		equipeFound.setModeloReuniao(request.getModeloReuniao());
		equipeFound.setLinkReuniaoOnline(request.getLinkReuniaoOnline());
		equipeFound.setStatusEquipe(request.getStatusEquipe());
		equipeFound.setAtualizadoEm(LocalDateTime.now());

		equipeRepository.save(equipeFound);

		return equipeMapper.toResponse(equipeFound);
	}

	@Transactional
	public EquipeResponseDto cadastrarEquipe(EquipeRequestDto request) {

		equipeRepository.findByNomeEquipe(FormataString.primeiraLetraMaiuscula(request.getNomeEquipe()))
				.ifPresent(c -> {
					throw new RegraNegocioException("Este nome de equipe já existe.");
				});

		var novaEquipe = equipeMapper.toEntity(request);
		novaEquipe.setDataPrevisaoLancamento(request.getDataInicioFormacao().plusDays(63));
		equipeRepository.save(novaEquipe);

		return equipeMapper.toResponse(novaEquipe);
	}

	@Transactional(readOnly = true)
	public EquipeResponseDto buscarEquipePorId(Long idEquipe) {
		var equipeFound = buscarEquipeEntity(idEquipe);

		return equipeMapper.toResponse(equipeFound);
	}

	@Transactional(readOnly = true)
	public Equipe buscarEquipeEntity(Long idEquipe) {
		return equipeRepository.findById(idEquipe)
				.orElseThrow(() -> new NaoEncontradoException("Equipe não encontrada."));
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<EquipeResponseDto> buscarTodasEquipes(Integer number, Integer size) {

		var pageable = PageRequest.of(number, size, Sort.by("nomeEquipe").ascending());

		var paginaFound = equipeRepository.findAll(pageable);

		var pageResponse = paginaFound.map(equipeMapper::toResponse);

		return paginacaoMapper.montarDtoResposta(pageResponse);

	}
}
