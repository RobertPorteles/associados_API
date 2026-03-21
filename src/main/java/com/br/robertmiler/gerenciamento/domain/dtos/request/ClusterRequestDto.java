package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClusterRequestDto {

	@NotBlank(message = "O nome do cluster é obrigatório.")
	private String nome;

}
