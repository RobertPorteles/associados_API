package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoGrupamentoRequestDto {

    @NotNull(message = "O associado é obrigatório.")
    private Long idAssociado;

    @NotNull(message = "O grupamento estratégico é obrigatório.")
    private Long idGrupamento;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @NotNull(message = "O status ativo é obrigatório.")
    private Boolean ativo;

}
