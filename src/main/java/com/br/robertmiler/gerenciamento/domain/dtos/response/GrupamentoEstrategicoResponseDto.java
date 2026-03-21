package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupamentoEstrategicoResponseDto {

    private Long idGrupamento;
    private String nomeGrupamento;
    private String sigla;
    private Boolean ativo;

}
