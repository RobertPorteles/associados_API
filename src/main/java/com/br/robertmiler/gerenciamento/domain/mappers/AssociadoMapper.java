package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;

@Component
public class AssociadoMapper {

	public AssociadoResponseDto montarDtoResposta(Associado associado) {
		AssociadoResponseDto response = new AssociadoResponseDto();
		response.setIdAssociado(associado.getIdAssociado());
		response.setNomeCompleto(associado.getNomeCompleto());
		response.setCpf(associado.getCpf());
		response.setNomeEquipe(associado.getEquipeAtual().getNomeEquipe());
		return response;
	}

}
