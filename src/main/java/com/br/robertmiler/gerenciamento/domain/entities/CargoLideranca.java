package com.br.robertmiler.gerenciamento.domain.entities;

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

import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;

@Getter
@Setter
@Entity
@Table(name = "cargos_lideranca")
public class CargoLideranca {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idCargoLideranca;

    @Column(name = "nome_cargo", unique = true)
    private String nomeCargo;

    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao_financeira")
    private ClassificacaoFinanceira classificacaoFinanceira;

    @Column(name = "ativo")
    private Boolean ativo;

}
