package com.br.robertmiler.gerenciamento.domain.entities;


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

   private String nome; // Ex: "Construção Civil" 
    private String sigla; // Ex: "CIVL" (Limite de 4 caracteres) 
    private boolean ativo; // Para permitir inativar 

    public GrupamentoEstrategico(String nome, String sigla) {
        if (sigla.length() != 4) {
            throw new IllegalArgumentException("A sigla deve ter exatamente 4 caracteres.");
        }
        this.nome = nome;
        this.sigla = sigla.toUpperCase();
        this.ativo = true;
    }

}
