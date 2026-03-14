package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDto {

	private Long idAssociado;
	private String razaoSocial;
	private String cnpj;
	private String nomeFantasia;
	private String cargo;

}
