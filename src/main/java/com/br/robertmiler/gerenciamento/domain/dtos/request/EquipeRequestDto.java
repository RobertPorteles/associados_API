package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.br.robertmiler.gerenciamento.domain.enums.DiaReuniao;
import com.br.robertmiler.gerenciamento.domain.enums.ModeloReuniao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeRequestDto {

	/**
	 * O nome da equipe deve obrigatoriamente iniciar com "C+C".
	 * Máximo de 20 caracteres. Único no sistema.
	 */
	@NotBlank(message = "O nome da equipe é obrigatório.")
	@Size(max = 20, message = "O nome da equipe deve ter no máximo 20 caracteres.")
	@Pattern(regexp = "(?i)c\\+c.*", message = "O nome da equipe deve iniciar com 'C+C'.")
	private String nomeEquipe;

	@NotNull(message = "A data de início de formação é obrigatória.")
	private LocalDate dataInicioFormacao;

	/**
	 * dataPrevisaoLancamento é calculada automaticamente (dataInicioFormacao + 63 dias).
	 * Não enviada pelo cliente — ignorada se presente no request.
	 */

	private LocalDate dataEfetivaLancamento;

	@NotNull(message = "O dia da reunião é obrigatório.")
	private DiaReuniao diaReuniao;

	@NotNull(message = "O horário da reunião é obrigatório.")
	private LocalTime horarioReuniao;

	@NotNull(message = "O modelo de reunião é obrigatório.")
	private ModeloReuniao modeloReuniao;

	@NotBlank(message = "O link de reunião online é obrigatório.")
	private String linkReuniaoOnline;

	private String statusEquipe;

}
