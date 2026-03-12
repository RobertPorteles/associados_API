package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;

@Service
public class AssociadoService {

	@Autowired
	private EquipeService equipeService;

	@Autowired
	private AssociadoRepository associadoRepository;
	
	@Transactional
	public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto request) {

		var equipeFound = equipeService.buscarEquipeEntity(request.getIdEquipe());

		Associado novoAssociado = new Associado();
		novoAssociado.setNomeCompleto(request.getNomeCompleto());
		novoAssociado.setCpf(request.getCpf());
		novoAssociado.setEquipeAtual(equipeFound);
		novoAssociado.setEquipeOrigem(equipeFound);
		
		associadoRepository.save(novoAssociado);
		
		

		AssociadoResponseDto response = new AssociadoResponseDto();
		response.setIdAssociado(novoAssociado.getIdAssociado());
		response.setNomeCompleto(novoAssociado.getNomeCompleto());
		response.setCpf(novoAssociado.getCpf());
		response.setNomeEquipe(novoAssociado.getEquipeAtual().getNomeEquipe());

		return response;

	}

}
