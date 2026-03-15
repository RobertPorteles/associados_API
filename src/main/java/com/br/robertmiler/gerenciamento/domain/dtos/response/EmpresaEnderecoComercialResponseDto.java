package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaEnderecoComercialResponseDto {

    private Long idEnderecoComercial;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String nomeFantasiaEmpresa;

}
