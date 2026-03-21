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
@Table(name = "grupamentos_estrategicos")
public class GrupamentoEstrategico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idGrupamento;

    @Column(name = "nome_grupamento", unique = true)
    private String nomeGrupamento;

    // Sigla de até 4 caracteres (ex: CIVL, IMOB, DIGI)
    @Column(name = "sigla", unique = true, length = 10)
    private String sigla;

    @Column(name = "ativo")
    private Boolean ativo;

}
