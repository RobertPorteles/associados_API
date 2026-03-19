package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.UserRule;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
     // busca ADM pelo email próprio do Usuario
    Optional<Usuario> findByEmail(String email);

    // busca Associado navegando pelo relacionamento Usuario → Associado → emailPrincipal
    Optional<Usuario> findByAssociado_EmailPrincipal(String email);
    //verificar se já existe, serve pro AdminSeeder
    boolean existsByRole(UserRule role);

}