package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.EquipeDiretorTerritorio;

public interface EquipeDiretorTerritorioRepository extends JpaRepository<EquipeDiretorTerritorio, Long> {

    List<EquipeDiretorTerritorio> findByEquipe_IdEquipe(Long idEquipe);

}
