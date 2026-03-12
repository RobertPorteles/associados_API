package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Equipe {
	public Long idEquipe;
	public String nomeEquipe;
	public LocalDate dataInicioFormacao;
	public LocalDate dataPrevisaoLancamento;
	public LocalDate dataEfetivaLancamento;
	public String diaReuniao;
	public LocalTime horarioReuniao;
	public String modeloReuniao;
	public String linkReuniaoOnline;
	public String statusEquipe;
	public LocalDateTime criadoEm;
	public LocalDateTime atualizadoEm;

}
