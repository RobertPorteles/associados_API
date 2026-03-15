package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoResponseDto {

	private Long idAssociado;

	private String nomeCompleto;

	private String cpf;

	private String emailPrincipal;

	private String telefonePrincipal;

	private Date dataNascimento;

	private Date dataIngresso;

	private Date dataVencimento;

	private String tipoOrigemEquipe;

	private Boolean statusAtivo;

	private String nomeEquipe;

	private String nomeCluster;

	private String nomeAtuacaoEspecifica;

}
