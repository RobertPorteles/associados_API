package com.br.robertmiler.gerenciamento.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipe")
public class Equipe {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long idEquipe;

	@Column
	public String nomeEquipe;

	/*
	 * @Column public LocalDate dataInicioFormacao;
	 * 
	 * @Column public LocalDate dataPrevisaoLancamento;
	 * 
	 * @Column public LocalDate dataEfetivaLancamento;
	 * 
	 * @Column public String diaReuniao;
	 * 
	 * public LocalTime horarioReuniao;
	 * 
	 * public String modeloReuniao;
	 * 
	 * public String linkReuniaoOnline;
	 * 
	 * public String statusEquipe;
	 * 
	 * public LocalDateTime criadoEm;
	 * 
	 * public LocalDateTime atualizadoEm;
	 */

}
