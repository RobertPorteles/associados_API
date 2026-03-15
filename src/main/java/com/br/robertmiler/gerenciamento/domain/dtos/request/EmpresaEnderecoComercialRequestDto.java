package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaEnderecoComercialRequestDto {

    private Long idEmpresa;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

}
