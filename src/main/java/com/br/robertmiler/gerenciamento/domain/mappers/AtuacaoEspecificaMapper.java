package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.response.AtuacaoEspecificaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AtuacaoEspecifica;

@Component
public class AtuacaoEspecificaMapper {

	public AtuacaoEspecificaResponseDto montarDtoResposta(AtuacaoEspecifica atuacaoEspecifica) {
		AtuacaoEspecificaResponseDto response = new AtuacaoEspecificaResponseDto();
		response.setId(atuacaoEspecifica.getId());
		response.setNome(atuacaoEspecifica.getNome());
		response.setNomeCluster(atuacaoEspecifica.getCluster().getNome());
		return response;
	}

}
