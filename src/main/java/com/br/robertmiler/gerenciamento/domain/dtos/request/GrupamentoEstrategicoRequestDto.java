package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupamentoEstrategicoRequestDto {

    @NotBlank(message = "O nome do grupamento é obrigatório.")
    private String nomeGrupamento;

    @NotBlank(message = "A sigla é obrigatória.")
    @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres.")
    private String sigla;

    @NotNull(message = "O status ativo é obrigatório.")
    private Boolean ativo;

}
