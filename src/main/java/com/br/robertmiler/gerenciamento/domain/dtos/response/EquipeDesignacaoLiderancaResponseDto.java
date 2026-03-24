package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeDesignacaoLiderancaResponseDto {

    private Long idDesignacao;
    private Long idEquipe;
    private String nomeEquipe;
    private Long idCargoLideranca;
    private String nomeCargoLideranca;
    private Long idAssociado;
    private String nomeAssociado;
    private LocalDate dataInicio;
    /** Nulo indica vigência em aberto. */
    private LocalDate dataFim;

}
