package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.Cluster;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {

	Optional<Cluster> findByNome(String nome);

	Page<Cluster> findAll(Pageable pageable);

}
