package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;

@Getter
@Setter
public class CargoLiderancaResponseDto {

    private Long idCargoLideranca;
    private String nomeCargo;
    private ClassificacaoFinanceira classificacaoFinanceira;
    private Boolean ativo;

}
