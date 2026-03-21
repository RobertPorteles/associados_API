package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoCargoLiderancaRequestDto {

    private Long idAssociado;
    private Long idCargoLideranca;
    private Date dataInicio;
    private Date dataFim;
    private Boolean ativo;

}
