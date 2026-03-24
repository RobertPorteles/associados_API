package com.br.robertmiler.gerenciamento.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoVisibilidadeResponseDto {

    private Long idVisibilidade;

    private String nomeAssociado;

    private boolean exibirAniversario;

    private boolean exibirEnderecoComercial;

}
