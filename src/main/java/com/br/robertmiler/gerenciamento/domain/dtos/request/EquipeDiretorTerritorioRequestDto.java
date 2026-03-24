package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeDiretorTerritorioRequestDto {

    @NotNull(message = "A equipe é obrigatória.")
    @Positive(message = "O ID da equipe deve ser um número positivo.")
    private Long idEquipe;

    @NotNull(message = "O associado é obrigatório.")
    @Positive(message = "O ID do associado deve ser um número positivo.")
    private Long idAssociado;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate dataInicio;

    /** Nulo indica vigência indeterminada. */
    private LocalDate dataFim;

}
