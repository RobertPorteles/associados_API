package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoResponseDto {
	
	private Long idAssociado;
	
	private String nomeCompleto;

	private String cpf;

	private String nomeEquipe;

}
