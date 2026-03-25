package com.br.robertmiler.gerenciamento.domain.enums;

public enum ClassificacaoFinanceira {
    NORMAL("Normal"),
    ISENTO("Isento"),
    NIVEL_1("Nível 1"),
    NIVEL_2("Nível 2"),
    NIVEL_3("Nível 3");

    private final String descricao;

    ClassificacaoFinanceira(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
