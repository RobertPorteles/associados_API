package com.br.robertmiler.gerenciamento.domain.enums;

public enum ClassificacaoFinanceira {
    NORMAL("Normal"),
    ISENTO("Isento");

    private final String descricao;

    ClassificacaoFinanceira(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
