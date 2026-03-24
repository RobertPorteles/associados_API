package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;
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
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean ativo;

}
