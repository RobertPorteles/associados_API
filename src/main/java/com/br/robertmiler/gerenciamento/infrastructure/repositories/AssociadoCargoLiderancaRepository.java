package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.AssociadoCargoLideranca;

@Repository
public interface AssociadoCargoLiderancaRepository extends JpaRepository<AssociadoCargoLideranca, Long> {

    List<AssociadoCargoLideranca> findByAssociado_IdAssociado(Long idAssociado);

    List<AssociadoCargoLideranca> findByCargoLideranca_IdCargoLideranca(Long idCargoLideranca);

}
