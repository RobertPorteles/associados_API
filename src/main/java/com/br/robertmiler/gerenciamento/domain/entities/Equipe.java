package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.br.robertmiler.gerenciamento.domain.enums.DiaReuniao;
import com.br.robertmiler.gerenciamento.domain.enums.ModeloReuniao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	private Long idEquipe;

	@Column(name = "nome_equipe", unique = true)
	private String nomeEquipe;

	@Column(name = "data_inicio_formacao")
	private LocalDate dataInicioFormacao;

	/**
	 * Calculado automaticamente: dataInicioFormacao + 63 dias.
	 * Recalculado sempre que dataInicioFormacao for alterado.
	 */
	@Column(name = "data_previsao_lancamento")
	private LocalDate dataPrevisaoLancamento;

	@Column(name = "data_efetiva_lancamento")
	private LocalDate dataEfetivaLancamento;

	@Enumerated(EnumType.STRING)
	@Column(name = "dia_reuniao")
	private DiaReuniao diaReuniao;

	@Column(name = "horario_reuniao")
	private LocalTime horarioReuniao;

	@Enumerated(EnumType.STRING)
	@Column(name = "modelo_reuniao")
	private ModeloReuniao modeloReuniao;

	@Column(name = "link_reuniao_online")
	private String linkReuniaoOnline;

	@Column(name = "status_equipe")
	private String statusEquipe;

	@Column(name = "criado_em")
	private LocalDateTime criadoEm;

	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;
}
