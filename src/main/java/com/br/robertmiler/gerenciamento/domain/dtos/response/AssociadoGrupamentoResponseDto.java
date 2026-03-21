package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoGrupamentoResponseDto {

    private Long idAssociadoGrupamento;
    private String nomeAssociado;
    private String nomeGrupamento;
    private String sigla;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean ativo;

}
