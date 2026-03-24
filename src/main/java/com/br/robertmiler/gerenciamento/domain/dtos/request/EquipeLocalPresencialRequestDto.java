package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeLocalPresencialRequestDto {

    @NotNull(message = "A equipe é obrigatória.")
    @Positive(message = "O ID da equipe deve ser um número positivo.")
    private Long idEquipe;

    @NotBlank(message = "A rua é obrigatória.")
    @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres.")
    private String rua;

    @NotBlank(message = "O número é obrigatório.")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
    private String numero;

    @Size(max = 80, message = "O complemento deve ter no máximo 80 caracteres.")
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
    private String cidade;

    @NotBlank(message = "A UF é obrigatória.")
    @Size(min = 2, max = 2, message = "A UF deve ser a sigla com exatamente 2 caracteres.")
    private String uf;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.")
    private String cep;

}
