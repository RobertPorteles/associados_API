package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeLocalPresencialResponseDto {

    private Long idLocalPresencial;
    private Long idEquipe;
    private String nomeEquipe;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

}
