package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.PerfilAssociado;

public interface PerfilAssociadoRepository extends JpaRepository<PerfilAssociado, Long> {

    Optional<PerfilAssociado> findByAssociado_IdAssociado(Long idAssociado);

    boolean existsByAssociado_IdAssociado(Long idAssociado);

}
