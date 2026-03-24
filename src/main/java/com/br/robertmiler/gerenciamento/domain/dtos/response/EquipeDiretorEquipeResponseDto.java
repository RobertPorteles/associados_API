package com.br.robertmiler.gerenciamento.domain.dtos.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeDiretorEquipeResponseDto {

    private Long idDiretorEquipe;
    private Long idEquipe;
    private String nomeEquipe;
    private Long idAssociado;
    private String nomeAssociado;
    private LocalDate dataInicio;
    /** Nulo indica vigência indeterminada. */
    private LocalDate dataFim;

}
