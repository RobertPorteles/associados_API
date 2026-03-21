package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.AssociadoGrupamento;

@Repository
public interface AssociadoGrupamentoRepository extends JpaRepository<AssociadoGrupamento, Long> {

    List<AssociadoGrupamento> findByAssociado_IdAssociado(Long idAssociado);

    List<AssociadoGrupamento> findByGrupamento_IdGrupamento(Long idGrupamento);

}
