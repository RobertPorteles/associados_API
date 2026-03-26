package com.br.robertmiler.gerenciamento.domain.dtos.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenovacaoAnuidadeRequestDto {

    @NotNull(message = "A data de pagamento é obrigatória.")
    @PastOrPresent(message = "A data de pagamento não pode ser futura.")
    private LocalDate dataPagamento;

}
