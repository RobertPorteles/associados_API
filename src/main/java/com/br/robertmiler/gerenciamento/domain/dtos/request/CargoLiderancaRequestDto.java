package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoLiderancaRequestDto {

    private String nomeCargo;

    // Valores aceitos: NORMAL, ISENTO
    private String classificacaoFinanceira;

    private Boolean ativo;

}
