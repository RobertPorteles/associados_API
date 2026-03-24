package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabela de pontuação mensal por faixa de associados ativos.
 * Configurável pela ADM. Alterações não retroagem no histórico.
 */
@Getter
@Setter
@Entity
@Table(name = "equipe_pontuacao_faixa")
public class EquipePontuacaoFaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idFaixa;

    @Column(name = "minimo_associados", nullable = false)
    private Integer minimoAssociados;

    /**
     * Nulo indica sem limite superior (faixa aberta para cima).
     * Ex: faixa "65+" tem maximoAssociados = null.
     */
    @Column(name = "maximo_associados")
    private Integer maximoAssociados;

    @Column(name = "pontos", nullable = false)
    private Integer pontos;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

}
