package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoRequestDto {

	private String nomeCompleto;

	private String cpf;

	private Long idEquipe;

	private Long idCluster;

	private String rua;

	private String numero;

	private String complemento;

	private String bairro;

	private String cidade;

	private String estado;

	private String cep;

}
