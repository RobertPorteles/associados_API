package com.br.robertmiler.gerenciamento.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "empresa")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idEmpresa;

	@Column(name = "razaoSocial")
	private String razaoSocial;

	@Column(name = "cnpj", unique = true)
	private String cnpj;

	@Column(name = "nomeFantasia")
	private String nomeFantasia;

	@Column(name = "cargo")
	private String cargo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "associado_id", nullable = false)
	private Associado associado;

}
