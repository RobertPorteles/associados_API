package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeRequestDto {

	@NotBlank(message = "O nome da equipe é obrigatório.")
	public String nomeEquipe;

	/*
	public LocalDate dataInicioFormacao;

	public LocalDate dataPrevisaoLancamento;

	public LocalDate dataEfetivaLancamento;
	
	public String diaReuniao;

	public LocalTime horarioReuniao;

	public String modeloReuniao;

	public String linkReuniaoOnline;

	public String statusEquipe;

	public LocalDateTime criadoEm;

	public LocalDateTime atualizadoEm;*/

}
