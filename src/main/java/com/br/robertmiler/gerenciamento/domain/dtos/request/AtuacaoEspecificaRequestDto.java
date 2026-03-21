package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtuacaoEspecificaRequestDto {

	@NotNull(message = "O cluster é obrigatório.")
	private Long idCluster;

	@NotBlank(message = "O nome da atuação específica é obrigatório.")
	private String nome;

}
