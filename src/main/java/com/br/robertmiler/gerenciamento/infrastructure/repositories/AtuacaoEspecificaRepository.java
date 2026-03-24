package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.AtuacaoEspecifica;

@Repository
public interface AtuacaoEspecificaRepository extends JpaRepository<AtuacaoEspecifica, Long> {

    Optional<AtuacaoEspecifica> findByNomeAndCluster_IdCluster(String nome, Long idCluster);

    Page<AtuacaoEspecifica> findByCluster_IdCluster(Long idCluster, Pageable pageable);

    Page<AtuacaoEspecifica> findAll(Pageable pageable);

}
