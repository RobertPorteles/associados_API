package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoCargoLiderancaRequestDto {

    @NotNull(message = "O associado é obrigatório.")
    private Long idAssociado;

    @NotNull(message = "O cargo de liderança é obrigatório.")
    private Long idCargoLideranca;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @NotNull(message = "O status ativo é obrigatório.")
    private Boolean ativo;

}
