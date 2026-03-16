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
@Table(name = "empresa_endereco_comercial")
public class EmpresaEnderecoComercial {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idEnderecoComercial;

	@Column
	private String rua;

	@Column
	private String numero;

	@Column
	private String complemento;

	@Column
	private String bairro;

	@Column
	private String cidade;

	@Column
	private String estado;

	@Column
	private String cep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

}
