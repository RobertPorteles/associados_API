package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipePontuacaoFaixaResponseDto {

    private Long idFaixa;
    private Integer minimoAssociados;
    /** Nulo indica sem limite superior. */
    private Integer maximoAssociados;
    private Integer pontos;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
