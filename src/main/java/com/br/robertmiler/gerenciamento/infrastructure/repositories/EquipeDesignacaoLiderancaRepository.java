package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.EquipeDesignacaoLideranca;

public interface EquipeDesignacaoLiderancaRepository extends JpaRepository<EquipeDesignacaoLideranca, Long> {

    List<EquipeDesignacaoLideranca> findByEquipe_IdEquipe(Long idEquipe);

    List<EquipeDesignacaoLideranca> findByEquipe_IdEquipeAndCargoLideranca_IdCargoLideranca(Long idEquipe,
            Long idCargoLideranca);

}
