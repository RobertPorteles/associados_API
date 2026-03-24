package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.EquipeLocalPresencial;

public interface EquipeLocalPresencialRepository extends JpaRepository<EquipeLocalPresencial, Long> {

    Optional<EquipeLocalPresencial> findByEquipe_IdEquipe(Long idEquipe);

}
