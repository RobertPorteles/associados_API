package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EquipeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EquipeResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Equipe;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeRepository;

@Service
public class EquipeService {

	@Autowired
	private EquipeRepository equipeRepository;

	public EquipeResponseDto cadastrarEquipe(EquipeRequestDto request) {

		Equipe novaEquipe = new Equipe();
		novaEquipe.setNomeEquipe(request.getNomeEquipe());
		equipeRepository.save(novaEquipe);

		EquipeResponseDto response = new EquipeResponseDto();
		response.setIdEquipe(novaEquipe.getIdEquipe());
		response.setNomeEquipe(novaEquipe.getNomeEquipe());

		return response;
	}

	public Equipe buscarEquipeEntity(Long idEquipe) {
		var equipeFound = equipeRepository.findById(idEquipe)
				.orElseThrow(() -> new NaoEncontradoException("Equipe não encontrada."));

		return equipeFound;
	}

}
