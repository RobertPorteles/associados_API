package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoCargoLiderancaResponseDto {

    private Long idAssociadoCargo;
    private String nomeAssociado;
    private String nomeCargo;
    private String classificacaoFinanceira;
    private Date dataInicio;
    private Date dataFim;
    private Boolean ativo;

}
