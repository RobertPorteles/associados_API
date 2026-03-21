package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;

@Getter
@Setter
public class AssociadoCargoLiderancaResponseDto {

    private Long idAssociadoCargo;
    private String nomeAssociado;
    private String nomeCargo;
    private ClassificacaoFinanceira classificacaoFinanceira;
    private Date dataInicio;
    private Date dataFim;
    private Boolean ativo;

}
