package com.br.robertmiler.gerenciamento.infrastructure.repositories;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long>{

    Optional<Equipe> findByNomeEquipe(String nomeEquipe);

    Page<Equipe> findAll(Pageable pageable);

}
