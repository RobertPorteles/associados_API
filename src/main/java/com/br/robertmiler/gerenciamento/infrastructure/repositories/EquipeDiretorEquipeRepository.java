package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.EquipeDiretorEquipe;

public interface EquipeDiretorEquipeRepository extends JpaRepository<EquipeDiretorEquipe, Long> {

    List<EquipeDiretorEquipe> findByEquipe_IdEquipe(Long idEquipe);

}
