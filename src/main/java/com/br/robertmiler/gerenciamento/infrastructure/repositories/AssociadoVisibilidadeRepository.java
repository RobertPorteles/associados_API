package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.AssociadoVisibilidade;

@Repository
public interface AssociadoVisibilidadeRepository extends JpaRepository<AssociadoVisibilidade, Long> {

    Optional<AssociadoVisibilidade> findByAssociado_IdAssociado(Long idAssociado);

    boolean existsByAssociado_IdAssociado(Long idAssociado);

}
