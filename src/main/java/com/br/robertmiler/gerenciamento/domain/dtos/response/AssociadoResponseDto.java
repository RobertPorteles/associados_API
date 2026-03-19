package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;

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
	/*
	Não faço a minima ideia da diferença entre "dataIngresso & dataVencimento" para "cridaoEm & atualizadoEm (Verificar a regra!)" 
	*/
	private LocalDate dataIngresso;

	private LocalDate dataVencimento;

	private String tipoOrigemEquipe;

	private StatusAssociado statusAssociado;

	private String nomeEquipe;

	private String nomeCluster;

	private String nomeAtuacaoEspecifica;

	private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

}
