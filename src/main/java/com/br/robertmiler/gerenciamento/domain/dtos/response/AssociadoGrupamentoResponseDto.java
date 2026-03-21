package com.br.robertmiler.gerenciamento.domain.dtos.response;

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
    private Date dataInicio;
    private Date dataFim;
    private Boolean ativo;

}
