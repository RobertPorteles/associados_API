package com.br.robertmiler.gerenciamento.domain.enums;

public enum ModeloReuniao {
    HIBRIDO("Híbrido (1ª e 3ª Presencial, demais Online)");

    private final String descricao;

    ModeloReuniao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
