package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoRequestDto {

	private String nomeCompleto;

	private String cpf;

	private Long idEquipe;

}
