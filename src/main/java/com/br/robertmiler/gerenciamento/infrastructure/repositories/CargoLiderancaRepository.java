package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.robertmiler.gerenciamento.domain.entities.CargoLideranca;

@Repository
public interface CargoLiderancaRepository extends JpaRepository<CargoLideranca, Long> {

    Optional<CargoLideranca> findByNomeCargo(String nomeCargo);

    Page<CargoLideranca> findAll(Pageable pageable);

}
