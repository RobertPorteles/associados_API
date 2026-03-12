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

@Entity
@Table(name = "Associados")
@Getter
@Setter
public class Associado {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idAssociado;

	@Column(name = "nome_completo", unique = true)
	private String nomeCompleto;

	@Column(name = "cpf", unique = true)
	private String cpf;

	/*
	 * @Column(name = "data_nascimento") private Date data_nascimento;
	 * 
	 * @Column(name = "email_principal") private String email_principal;
	 * 
	 * @Column(name = "telefone_principal") private String telefone_principal;
	 * 
	 * @Column(name = "data_ingresso") private Date data_ingresso;
	 * 
	 * @Column(name = "data_vencimento") private Date data_vencimento;
	 * 
	 * @Column(name = "tipo_origem_equipe") private String tipo_origem_equipe;
	 * 
	 * @Column(name = "status_ativo") private Boolean status_ativo;
	 * 
	 * @Column(name = "criado_em") private Timestamp criado_em;
	 * 
	 * @Column(name = "atualizado_em") private Timestamp atualizado_em;
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipe_origem_id", nullable = false)
	private Equipe equipeOrigem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipe_atual_id", nullable = false)
	private Equipe equipeAtual;

}
/*
 * 
 * - - - id_equipe **BIGINT (FK)** - id_cluster **BIGINT (FK)** -
 * id_atuacao_especifica **BIGINT (FK)** - id_padrinho **BIGINT (FK)**
 * 
 * - tipo_origem_equipe **VARCHAR(30)**
 * 
 * - id_equipe_origem **BIGINT (FK)**
 * 
 * - status_ativo **BOOLEAN** - criado_em **TIMESTAMP** - atualizado_em
 * **TIMESTAMP** }
 */