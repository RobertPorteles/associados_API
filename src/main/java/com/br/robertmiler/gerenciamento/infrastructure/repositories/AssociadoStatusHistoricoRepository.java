package com.br.robertmiler.gerenciamento.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.robertmiler.gerenciamento.domain.entities.AssociadoStatusHistorico;

public interface AssociadoStatusHistoricoRepository extends JpaRepository<AssociadoStatusHistorico, Long> {

    List<AssociadoStatusHistorico> findByAssociado_IdAssociadoOrderByRegistradoEmDesc(Long idAssociado);

}
