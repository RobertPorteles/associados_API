package com.br.robertmiler.gerenciamento.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoEnderecoResidencialRequestDto {

    @NotNull(message = "O associado é obrigatório.")
    private Long idAssociado;

    @NotBlank(message = "A rua é obrigatória.")
    private String rua;

    @NotBlank(message = "O número é obrigatório.")
    private String numero;

    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    private String estado;

    @NotBlank(message = "O CEP é obrigatório.")
    private String cep;

}
