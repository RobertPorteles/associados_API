package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.br.robertmiler.gerenciamento.domain.enums.DiaReuniao;
import com.br.robertmiler.gerenciamento.domain.enums.ModeloReuniao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeResponseDto {

	private Long idEquipe;

	private String nomeEquipe;

	private LocalDate dataInicioFormacao;

	private LocalDate dataPrevisaoLancamento;

	private LocalDate dataEfetivaLancamento;

	private DiaReuniao diaReuniao;

	private LocalTime horarioReuniao;

	private ModeloReuniao modeloReuniao;

	private String linkReuniaoOnline;

	private String statusEquipe;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

}
