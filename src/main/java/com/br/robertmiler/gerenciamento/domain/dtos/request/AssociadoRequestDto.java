package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoRequestDto {

	private String nomeCompleto;

	private String cpf;

	private String emailPrincipal;

	private String telefonePrincipal;

	private LocalDate dataNascimento;

	private LocalDate dataIngresso;

	private LocalDate dataVencimento;

	private String tipoOrigemEquipe;

	private Boolean statusAtivo;

	private Long idEquipe;

	private Long idCluster;

	private Long idAtuacaoEspecifica;

	private String rua;

	private String numero;

	private String complemento;

	private String bairro;

	private String cidade;

	private String estado;

	private String cep;

}
