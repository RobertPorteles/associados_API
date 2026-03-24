package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaResponseDto {

	private Long idEmpresa;
	private String razaoSocial;
	private String cnpj;
	private String nomeFantasia;
	private String nomeAssociado;

}
