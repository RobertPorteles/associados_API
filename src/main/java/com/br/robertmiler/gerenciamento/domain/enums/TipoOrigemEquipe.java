package com.br.robertmiler.gerenciamento.domain.enums;

public enum TipoOrigemEquipe {
    ORIGINAL("Original"),
    COLABORATIVA("Colaborativa");

    private final String descricao;

    TipoOrigemEquipe(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
