package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;

import com.br.robertmiler.gerenciamento.domain.enums.StatusAssociado;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarStatusAssociadoRequestDto {

    @NotNull(message = "O novo status é obrigatório.")
    private StatusAssociado statusNovo;

    /**
     * Obrigatório para: INATIVO_DESISTENCIA e INATIVO_DESLIGADO.
     * Opcional nos demais status.
     * Validado em nível de service.
     */
    private String motivo;

    /**
     * Obrigatório quando statusNovo = INATIVO_PAUSA_PROGRAMADA.
     */
    private LocalDate dataInicioPausa;

    /**
     * Obrigatório quando statusNovo = INATIVO_PAUSA_PROGRAMADA.
     */
    private LocalDate dataPrevisaoRetorno;

    /**
     * ID do usuário (ADM) que está realizando a alteração.
     * Futuramente pode ser extraído automaticamente do SecurityContext.
     */
    @NotNull(message = "O usuário responsável pela alteração é obrigatório.")
    @Positive(message = "O ID do usuário deve ser um número positivo.")
    private Long idRegistradoPor;

}
