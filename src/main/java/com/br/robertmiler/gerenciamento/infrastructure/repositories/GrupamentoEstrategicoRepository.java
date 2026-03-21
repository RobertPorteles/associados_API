package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.GrupamentoEstrategico;

@Repository
public interface GrupamentoEstrategicoRepository extends JpaRepository<GrupamentoEstrategico, Long> {

    Optional<GrupamentoEstrategico> findByNomeGrupamento(String nomeGrupamento);

    Optional<GrupamentoEstrategico> findBySigla(String sigla);

    Page<GrupamentoEstrategico> findAll(Pageable pageable);

}
