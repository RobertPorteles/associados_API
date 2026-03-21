package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoGrupamentoRequestDto {

    private Long idAssociado;
    private Long idGrupamento;
    private Date dataInicio;
    private Date dataFim;
    private Boolean ativo;

}
