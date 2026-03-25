package com.br.robertmiler.gerenciamento.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "associado_status_historico")
public class AssociadoStatusHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idStatusHistorico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id", nullable = false)
    private Associado associado;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 50)
    private StatusAssociado statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 50)
    private StatusAssociado statusNovo;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    /**
     * Preenchido apenas quando statusNovo = INATIVO_PAUSA_PROGRAMADA.
     */
    @Column(name = "data_inicio_pausa")
    private LocalDate dataInicioPausa;

    /**
     * Preenchido apenas quando statusNovo = INATIVO_PAUSA_PROGRAMADA.
     */
    @Column(name = "data_previsao_retorno")
    private LocalDate dataPrevisaoRetorno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por", nullable = false)
    private Usuario registradoPor;

    @Column(name = "registrado_em", nullable = false, updatable = false)
    private LocalDateTime registradoEm;

    @PrePersist
    private void prePersist() {
        this.registradoEm = LocalDateTime.now();
    }

}
