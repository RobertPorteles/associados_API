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
    @Size(min = 2, max = 4, message = "A sigla deve ter entre 2 e 4 caracteres.")
    private String sigla;

    @NotNull(message = "O status ativo é obrigatório.")
    private Boolean ativo;

}
