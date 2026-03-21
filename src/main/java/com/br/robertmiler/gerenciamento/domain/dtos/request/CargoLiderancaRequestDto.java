package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;

@Getter
@Setter
public class CargoLiderancaRequestDto {

    private String nomeCargo;

    private ClassificacaoFinanceira classificacaoFinanceira;

    private Boolean ativo;

}
