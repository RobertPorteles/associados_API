package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "equipe")
@Getter
@Setter
public class Equipe {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long idEquipe;

	@Column(name = "nomeEquipe", unique = true)
	public String nomeEquipe;

	@Column(name = "dataInicioFormacao")
	public LocalDate dataInicioFormacao;

	@Column(name = "dataPrevisaoLancamento")
	public LocalDate dataPrevisaoLancamento;

	@Column(name = "dataEfetivaLancamento")
	public LocalDate dataEfetivaLancamento;
	
	@Column(name = "diaReuniao")
	public String diaReuniao;

	@Column(name = "horarioReuniao")
	public LocalTime horarioReuniao;

	@Column(name = "modeloReuniao")
	public String modeloReuniao;

	@Column(name = "linkReuniaoOnline")
	public String linkReuniaoOnline;

	@Column(name = "statusEquipe")
	public String statusEquipe;

	@Column(name = "criadoEm")
	public LocalDateTime criadoEm;

	@Column(name = "atualizadoEm")
	public LocalDateTime atualizadoEm;
}
