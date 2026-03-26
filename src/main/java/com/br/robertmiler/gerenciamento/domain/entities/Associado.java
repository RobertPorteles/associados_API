package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.br.robertmiler.gerenciamento.domain.annotations.Visibilidade;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;
import com.br.robertmiler.gerenciamento.domain.enums.TipoOrigemEquipe;

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
import jakarta.persistence.OneToOne;
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

    // ── Leitura: ADM_CC e o próprio ASSOCIADO; Inserção/Edição: apenas ADM_CC ────
    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    @Column(name = "cpf", unique = true, length = 11, nullable = false)
    private String cpf;

    // ── Equipe atual: leitura pública (REDE_CC); edição apenas ADM_CC ────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_atual_id", nullable = false)
    private Equipe equipeAtual;

    // ── Data de nascimento: ASSOCIADO edita a si mesmo; ADM_CC sempre ────────────
    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    // ── E-mail: ASSOCIADO edita o próprio; unicidade validada no service ─────────
    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    @Column(name = "email_principal")
    private String emailPrincipal;

    @Visibilidade(
        leitura  = {Perfil.ADM_CC, Perfil.ASSOCIADO},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC, Perfil.ASSOCIADO}
    )
    @Column(name = "telefone_principal")
    private String telefonePrincipal;

    // ── dataIngresso: leitura REDE_CC (pública); edição restrita a ADM_CC ────────
    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    @Column(name = "data_ingresso")
    private LocalDate dataIngresso;

    /**
     * Calculada automaticamente: nunca recebida do cliente.
     * dataIngresso + 1 mês → primeiro dia → +1 ano.
     * Editável apenas via renovação (ADM_CC).
     */
    @Visibilidade(
        leitura  = {Perfil.REDE_CC},
        insercao = {},
        edicao   = {Perfil.ADM_CC}
    )
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_origem_equipe", nullable = false)
    private TipoOrigemEquipe tipoOrigemEquipe;

    /**
     * Status: visibilidade varia pelo valor — ver VisibilidadeFiltroService.
     * TODA alteração DEVE ser feita exclusivamente por ADM_CC via endpoint explícito.
     */
    @Visibilidade(
        leitura  = {Perfil.REDE_CC, Perfil.ADM_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "status_associado")
    private StatusAssociado statusAssociado;

    /** Preenchido apenas quando statusAssociado = INATIVO_PAUSA_PROGRAMADA. */
    @Column(name = "data_inicio_pausa")
    private LocalDate dataInicioPausa;

    /** Preenchido apenas quando statusAssociado = INATIVO_PAUSA_PROGRAMADA. */
    @Column(name = "data_previsao_retorno")
    private LocalDate dataPrevisaoRetorno;

    // ── Primeiro pagamento: apenas ADM_CC enxerga e edita ────────────────────────
    @Visibilidade(
        leitura  = {Perfil.ADM_CC},
        insercao = {Perfil.ADM_CC},
        edicao   = {Perfil.ADM_CC}
    )
    @Column(name = "data_pagamento_primeira_anuidade")
    private LocalDate dataPagamentoPrimeiraAnuidade;

    @Column(name = "motivo_status_inativo")
    private String motivoStatusInativo;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // ── Cluster: leitura REDE_CC; edição ADM_CC ──────────────────────────────────
    @ManyToOne
    @JoinColumn(name = "id_cluster")
    private Cluster cluster;

    @ManyToOne
    @JoinColumn(name = "id_atuacao_especifica")
    private AtuacaoEspecifica atuacaoEspecifica;

    // ── Padrinho: leitura restrita a ADM_CC ──────────────────────────────────────
    @ManyToOne
    @JoinColumn(name = "id_padrinho")
    private Associado padrinho;

    // ── Equipe de origem: leitura restrita a ADM_CC ──────────────────────────────
    @ManyToOne
    @JoinColumn(name = "id_equipe_origem")
    private Equipe equipeOrigem;

    /**
     * Preferências de visibilidade do associado (exibirAniversario, exibirEnderecoComercial).
     * Mapeada como OneToOne inversa — a chave está em AssociadoVisibilidade.
     */
    @OneToOne(mappedBy = "associado", fetch = FetchType.LAZY)
    private AssociadoVisibilidade visibilidade;
}
