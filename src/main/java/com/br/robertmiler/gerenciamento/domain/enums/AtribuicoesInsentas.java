package com.br.robertmiler.gerenciamento.domain.enums;

public enum AtribuicoesInsentas {
DE_DIRETOR_DE_EQUIPE("D.E- Diretor de Equipe"),
DT3_DIRETOR_DE_TERRITORIO3("DT3- Diretor de Território 3"),
DT2_DIRETOR_DE_TERRITORIO2("DT2- Diretor de Território 2"),
DT1_DIRETOR_DE_TERRITORIO1("DT1- Diretor de Território 1"),
DM3_DIRETOR_MASTER3("DM3- Diretor Master 3"),
DM2_DIRETOR_MASTER2("DM2- Diretor Master 2"),
DM1_DIRETOR_MASTER1("DM1- Diretor Master 1"),
ADM_CC_ADMINISTRACAO("ADM C+C- Administração C+C");

private String atribuicaoIsentas;




public String getAtribuicaoIsentas() {
    return atribuicaoIsentas;
}



private AtribuicoesInsentas(String atribuicaoIsentas) {
    this.atribuicaoIsentas = atribuicaoIsentas;
}




}
