package com.br.robertmiler.gerenciamento.domain.enums;


public enum StatusAssociado {
    PREATIVO("Pré-ativo"),
    ATIVO("Ativo"),
    INATIVO_PAUSA_PROGRAMADA("Inativo - Pausa programada"),
    INATIVO_DESISTENCIA("Inativo - Desistência"),
    INATIVO_FALECIMENTO("Inativo - Falecimento"),
    INATIVO_DESLIGADO("Inativo - Desligado"),
    INATIVO_OUTRO("Inativo - Outro");


    private final String descricao;

    StatusAssociado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    
}
