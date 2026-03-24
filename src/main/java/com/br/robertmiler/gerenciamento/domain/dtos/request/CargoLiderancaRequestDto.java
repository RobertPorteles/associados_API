package com.br.robertmiler.gerenciamento.domain.dtos.request;

import com.br.robertmiler.gerenciamento.domain.enums.ClassificacaoFinanceira;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoLiderancaRequestDto {

    @NotBlank(message = "O nome do cargo é obrigatório.")
    private String nomeCargo;

    @NotNull(message = "A classificação financeira é obrigatória.")
    private ClassificacaoFinanceira classificacaoFinanceira;

    @NotNull(message = "O status ativo é obrigatório.")
    private Boolean ativo;

}
