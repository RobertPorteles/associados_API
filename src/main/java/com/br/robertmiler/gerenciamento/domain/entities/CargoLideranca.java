package com.br.robertmiler.gerenciamento.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "classificacao_financeira")
    private String classificacaoFinanceira;

    @Column(name = "ativo")
    private Boolean ativo;

}
