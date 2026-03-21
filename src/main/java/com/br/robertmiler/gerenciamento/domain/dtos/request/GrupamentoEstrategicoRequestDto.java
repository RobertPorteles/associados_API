package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupamentoEstrategicoRequestDto {

    private String nomeGrupamento;

    // Sigla de até 4 caracteres (ex: CIVL, IMOB, DIGI)
    private String sigla;

    private Boolean ativo;

}
