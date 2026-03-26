package com.br.robertmiler.gerenciamento.domain.enums;

/**
 * Perfis de acesso do sistema C+C.
 *
 * ADM_CC  - Nível 1. Acesso total. Único que insere e edita dados administrativos.
 * DIRETOR - Nível 2. Acesso a dados estruturais de equipe.
 * ASSOCIADO - Nível 3. Acesso ao próprio perfil e campos públicos da rede.
 * REDE_CC - Virtual: representa "toda a rede". Campos públicos visíveis a qualquer
 *           associado ativo autenticado.
 */
public enum Perfil {
    ADM_CC,
    DIRETOR,
    ASSOCIADO,
    REDE_CC
}
