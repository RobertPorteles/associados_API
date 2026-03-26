package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.enums.TipoOrigemEquipe;

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

	private LocalDate dataNascimento;

	/** Data em que o associado ingressou na rede C+C (dado de negócio, editável pela ADM). */
	private LocalDate dataIngresso;

	/** Data de vencimento da anuidade (dado de negócio, editável pela ADM). */
	private LocalDate dataVencimento;

	private TipoOrigemEquipe tipoOrigemEquipe;

	private StatusAssociado statusAssociado;

	/** Preenchido apenas quando statusAssociado = INATIVO_PAUSA_PROGRAMADA. */
	private LocalDate dataInicioPausa;

	/** Preenchido apenas quando statusAssociado = INATIVO_PAUSA_PROGRAMADA. */
	private LocalDate dataPrevisaoRetorno;

	private LocalDate dataPagamentoPrimeiraAnuidade;

	private String motivoStatusInativo;

	private String nomePadrinho;

	private String nomeEquipeOrigem;

	private String nomeEquipe;

	private String nomeCluster;

	private String nomeAtuacaoEspecifica;

	/** Timestamp de criação do registro no sistema (técnico, nunca editado). */
	private LocalDateTime criadoEm;

	/** Timestamp da última atualização do registro no sistema (técnico). */
	private LocalDateTime atualizadoEm;

}
