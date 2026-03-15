package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoEnderecoResidencialResponseDto {

    private Long idEndereco;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String nomeAssociado;

}
