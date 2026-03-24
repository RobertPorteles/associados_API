package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AtuacaoEspecificaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AtuacaoEspecificaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.AtuacaoEspecifica;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;

@Component
public class AtuacaoEspecificaMapper {

	public AtuacaoEspecifica toEntity(AtuacaoEspecificaRequestDto request) {
		AtuacaoEspecifica novaAtuacao = new AtuacaoEspecifica();
		novaAtuacao.setNome(FormataString.primeiraLetraMaiuscula(request.getNome()));

		return novaAtuacao;
	}

	public AtuacaoEspecificaResponseDto toResponse(AtuacaoEspecifica atuacaoEspecifica) {
		AtuacaoEspecificaResponseDto response = new AtuacaoEspecificaResponseDto();
		response.setId(atuacaoEspecifica.getId());
		response.setNome(atuacaoEspecifica.getNome());
		response.setNomeCluster(atuacaoEspecifica.getCluster().getNome());

		return response;
	}

}
