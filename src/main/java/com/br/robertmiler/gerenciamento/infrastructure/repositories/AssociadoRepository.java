package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.Associado;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long>{

    Optional<Associado> findByCpf(String cpf);



    // verifica duplicidade de e-mail no cadastro
    Optional<Associado> findByEmailPrincipal(String emailPrincipal);

    boolean existsByCpf(String cpf);
}
