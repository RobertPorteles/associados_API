package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipePontuacaoFaixaRequestDto {

    @NotNull(message = "O mínimo de associados é obrigatório.")
    @Min(value = 1, message = "O mínimo de associados deve ser pelo menos 1.")
    private Integer minimoAssociados;

    /**
     * Nulo indica sem limite superior (faixa aberta para cima).
     */
    @Positive(message = "O máximo de associados deve ser um número positivo.")
    private Integer maximoAssociados;

    @NotNull(message = "A pontuação é obrigatória.")
    @Positive(message = "A pontuação deve ser um número positivo.")
    private Integer pontos;

    @NotNull(message = "O status ativo é obrigatório.")
    private Boolean ativo;

}
