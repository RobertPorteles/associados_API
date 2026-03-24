package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;

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
@Table(name = "equipe_designacao_lideranca")
public class EquipeDesignacaoLideranca {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idDesignacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_lideranca_id", nullable = false)
    private CargoLideranca cargoLideranca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id", nullable = false)
    private Associado associado;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    /** Nulo indica vigência em aberto. */
    @Column(name = "data_fim")
    private LocalDate dataFim;

}
