package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoLiderancaResponseDto {

    private Long idCargoLideranca;
    private String nomeCargo;
    private String classificacaoFinanceira;
    private Boolean ativo;

}
