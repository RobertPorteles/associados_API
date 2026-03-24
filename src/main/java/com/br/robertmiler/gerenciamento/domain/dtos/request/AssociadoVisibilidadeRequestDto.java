package com.br.robertmiler.gerenciamento.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoVisibilidadeRequestDto {

    private Long idAssociado;

    private boolean exibirAniversario;

    private boolean exibirEnderecoComercial;

}
