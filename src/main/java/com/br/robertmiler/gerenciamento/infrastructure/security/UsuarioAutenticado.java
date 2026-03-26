package com.br.robertmiler.gerenciamento.infrastructure.security;

import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Objeto armazenado como details da autenticação (UsernamePasswordAuthenticationToken).
 * Transporta o CPF e o Perfil do usuário autenticado para uso nos controllers e
 * serviços de visibilidade, sem precisar recarregar o usuário do banco.
 */
@Getter
@AllArgsConstructor
public class UsuarioAutenticado {

    /** CPF do associado autenticado; null quando o usuário é ADM_CC sem associado vinculado. */
    private final String cpf;

    /** Perfil de acesso determinado no momento do login. */
    private final Perfil perfil;
}
