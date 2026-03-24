package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.br.robertmiler.gerenciamento.domain.enums.AtribuicoesInsentas;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AssociadoLocal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idAssociado;

	@Column(name = "nome_completo")
	private String nomeCompleto;

	@Column(name = "cpf", unique = true)
	private String cpf;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipe_atual_id", nullable = false)
	private Equipe equipeAtual;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "email_principal")
    private String emailPrincipal;

    @Column(name = "telefone_principal")
    private String telefonePrincipal;

    @Column(name = "data_ingresso")
    private LocalDate dataIngresso;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "tipo_origem_equipe")
    private String tipoOrigemEquipe;

    @Enumerated(EnumType.STRING)         
    @Column(name = "status_associado")      
    private StatusAssociado statusAssociado;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "atribuicao_isentas")
    private AtribuicoesInsentas atribuicoesInsentas;
    //cluster

    @ManyToOne
    @JoinColumn(name = "id_cluster")
    private Cluster cluster;

    @ManyToOne
    @JoinColumn(name = "id_atuacao_especifica")
    private AtuacaoEspecifica atuacaoEspecifica;

    @ManyToOne
    @JoinColumn(name = "id_padrinho")
    private Associado padrinho;

    @ManyToOne
    @JoinColumn(name = "id_equipe_origem")
    private Equipe equipeOrigem;

 
}

   /*

- 
-
-   id_equipe **BIGINT (FK)**
-   id_cluster **BIGINT (FK)**
-   id_atuacao_especifica **BIGINT (FK)**
-   id_padrinho **BIGINT (FK)**

-   tipo_origem_equipe **VARCHAR(30)**

-   id_equipe_origem **BIGINT (FK)**

-   status_ativo **BOOLEAN**
-   criado_em **TIMESTAMP**
-   atualizado_em **TIMESTAMP**
}
 */